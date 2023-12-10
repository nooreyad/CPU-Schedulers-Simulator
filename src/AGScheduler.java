import java.util.*;

public class AGScheduler extends Scheduler{
    Map<Process, Integer> agFactor = new HashMap<>();
    Map<Process, Integer> quantumTime = new HashMap<>();
    Map<Process, Integer> burstTimeMp = new HashMap<>();
    Queue<Process> readyQueue = new LinkedList<>();
    int quantumVal = 4;

    boolean quantumCase = false;
    @Override
    public void setExecutionOrder(ArrayList<Process> processes)
    {

        // set an AG Factor and a quantum for each process
        for (Process process: processes) {
            agFactor.put(process, getAgFactor(process));
            quantumTime.put(process, quantumVal);
            burstTimeMp.put(process, process.getBurstTime());
        }
        // uncomment for specific AGFactor testing
//        {
//            agFactor.put(processes.get(0), 20);
//            agFactor.put(processes.get(1), 17);
//            agFactor.put(processes.get(2), 16);
//            agFactor.put(processes.get(3), 43);
//        }

        printInitialOutput();
        int currentTime = 0;
        while (!processes.isEmpty())
        {
            updateQueue(currentTime, processes);
            if(!readyQueue.isEmpty())
            {

                // process with least ag factor first
                Process currProcess;
                if(quantumCase)
                {
                    currProcess = readyQueue.poll();
                    quantumCase = false;
                }
                else
                {
                    currProcess = getMinProcess();
                }
                double nonPreemptive = quantumTime.get(currProcess);
                nonPreemptive = Math.ceil(nonPreemptive/2);
                if(currProcess.getEntranceTime() == -1) currProcess.setEntranceTime(currentTime);

//              The running process finished its job in the first half of quantum
                if(nonPreemptive >= currProcess.getBurstTime())
                {
                    currentTime += currProcess.getBurstTime();
                    currProcess.setBurstTime(0);
                    quantumTime.put(currProcess, 0);
                    printQuantum();
                    processes.remove(currProcess);
                    processQueue.add(currProcess);
                    currProcess.setCompletionTime(currentTime);
                    currProcess.setTurnaroundTime(currProcess.getCompletionTime() + currProcess.getArrivalTime());
                    currProcess.setWaitingTime(currProcess.getTurnaroundTime() - currProcess.getBurstTime());
                    readyQueue.remove(currProcess);
                    quantumCase = true;
                }
                else
                {
                    currentTime += nonPreemptive;
                    int idx = 0;
                    while (true)
                    {
                        updateQueue(currentTime, processes);
                        if(agFactor.get(currProcess) > agFactor.get(getMinProcess()))
                        {
//                          update the quantum time of the old process (add remaining time)
                            int quanTime = quantumTime.get(currProcess);
                            int remainTime = quanTime - (int)Math.ceil(quanTime/2.0) - idx;
                            quantumTime.put(currProcess, quanTime + remainTime);
                            printQuantum();

//                          update burst time of old process
                            currProcess.setBurstTime(currProcess.getBurstTime() - (int)nonPreemptive - idx);

//                          add old process to ready queue
                            if(!readyQueue.contains(currProcess)) readyQueue.add(currProcess);

//                          move to work on the new process
                            break;
                        }
                        else
                        {
                            int quanTime = quantumTime.get(currProcess);
                            int remainTime = quanTime - (int)Math.ceil(quanTime/2.0) - idx;
                            int burstTime = currProcess.getBurstTime() - (int)nonPreemptive - idx;

                            // The running process used all its quantum time
                            if(remainTime == 0 && burstTime != 0)
                            {
                                quantumCase = true;
                                // update the quantum time of the old process (10% of mean)|
                                double newQuantum = Math.ceil((1.0 / 10) * meanQuantum());
                                int oldQuantum  = quantumTime.get(currProcess);
                                quantumTime.put(currProcess, oldQuantum + (int)newQuantum);
                                printQuantum();

                                // update burst time of old process
                                currProcess.setBurstTime(currProcess.getBurstTime() - (int)nonPreemptive - idx);

                                // add old process to ready queue
                                if(!readyQueue.contains(currProcess)) readyQueue.add(currProcess);
                                break;
                            }
                            else if(burstTime == 0)
                            {
                                quantumCase = true;
                                quantumTime.put(currProcess, 0);
                                currProcess.setBurstTime(0);
                                processes.remove(currProcess);
                                processQueue.add(currProcess);
                                currProcess.setTurnaroundTime(currProcess.getWaitingTime() + burstTimeMp.get(currProcess));
                                readyQueue.remove(currProcess);
                                printQuantum();
                                break;
                            }
                            readyQueue.remove(currProcess);
                            idx++;
                            currentTime++;
                        }
                    }
                }
            }
            else
            {
                return;
            }
        }
        System.out.println();
    }


    public Integer getAgFactor(Process process){
        int agFactor;
        Random random = new Random();
        int randInt = random.nextInt(0, 21);
        if(randInt < 10) agFactor = randInt;
        else if(randInt > 10) agFactor = 10;
        else agFactor = process.getPriorityNumber();
        agFactor = agFactor + process.getArrivalTime() + process.getBurstTime();
        return agFactor;
    }

    public void updateQueue(int currentTime, ArrayList<Process> processes){
        for(int i = 0; i < processes.size(); i++){
            if(processes.get(i).getArrivalTime() <= currentTime && !readyQueue.contains(processes.get(i))){
                readyQueue.add(processes.get(i));
            }
        }
    }

    public void printQuantum(){
        Map<String, Integer> sortedTreeMap = new TreeMap<>();
        for (var entry: quantumTime.entrySet()) {
            sortedTreeMap.put(entry.getKey().getName(), entry.getValue());
        }
        int sz = sortedTreeMap.size();
        System.out.print("( ");
        for (var entry: sortedTreeMap.entrySet()) {
            sz--;
            if(sz == 0) System.out.print(entry.getValue() + " ");
            else System.out.print(entry.getValue() + " , ");
        }
        System.out.println(")");
    }

    public void printInitialOutput(){
        System.out.println("History update of quantum time for each process:");
        Map<String, Integer> sortedTreeMap = new TreeMap<>();
        for (var entry: quantumTime.entrySet()) {
            sortedTreeMap.put(entry.getKey().getName(), entry.getValue());
        }
        System.out.print(" ");
        int sz = sortedTreeMap.size();
        for (var entry: sortedTreeMap.entrySet()) {
            sz--;
            if(sz == 0) System.out.print(entry.getKey());
            else System.out.print(entry.getKey() + " , ");
        }
        System.out.println();
        printQuantum();
    }

    public double meanQuantum(){
        double sum = 0;
        int num = quantumTime.size();
        for (var entry: quantumTime.entrySet()) {
            sum += entry.getValue();
        }
        return sum/num;
    }

    public Process getMinProcess(){
        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt(l->agFactor.get(l)));
        pq.addAll(readyQueue);
        readyQueue.remove(pq.peek());
        return pq.peek();
    }
}
