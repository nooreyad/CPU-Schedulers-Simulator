import java.util.*;

public class AGScheduler extends Scheduler{
    Map<Process, Integer> agFactor = new HashMap<>();
    Map<Process, Integer> quantumTime = new HashMap<>();
    Map<Process, Integer> burstTimeMp = new HashMap<>();
    Queue<Process> readyQueue = new LinkedList<>();
    int quantumVal = 4;
    int startTime;
    int currentTime;
    boolean quantumCase = false;

    @Override
    public void setExecutionOrder(ArrayList<Process> processes)
    {
        int minArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).min().orElse(Integer.MAX_VALUE);
        int completionTime = processes.stream().mapToInt(Process::getBurstTime).sum() + minArrivalTime;
        currentTime =  minArrivalTime;

        // set an AG Factor and a quantum for each process
        for (Process process: processes) {
            agFactor.put(process, getAgFactor(process));
            quantumTime.put(process, quantumVal);
            burstTimeMp.put(process, process.getBurstTime());
        }

        // uncomment for specific AGFactor testing
        {
            agFactor.put(processes.get(0), 20);
            agFactor.put(processes.get(1), 17);
            agFactor.put(processes.get(2), 16);
            agFactor.put(processes.get(3), 43);
        }

        printInitialOutput();

        while (!processes.isEmpty())
        {
            updateQueue(currentTime, processes);
            if(!readyQueue.isEmpty())
            {
                // process with least ag factor first or next ready process if process used all of its quantum
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
                // each process initialization
                double nonPreemptive = quantumTime.get(currProcess);
                nonPreemptive = Math.ceil(nonPreemptive/2);
                startTime = currentTime;

                // set process first entrance time only
                if(currProcess.getEntranceTime() == -1) currProcess.setEntranceTime(currentTime);

                // The running process finished its job in the first half of quantum
                if(nonPreemptive >= burstTimeMp.get(currProcess))
                {
                    quantumCase = true;
                    currentTime += burstTimeMp.get(currProcess);
                    printQuantum();
                    endProcess(currProcess);
                    processes.remove(currProcess);
                }
                else
                {
                    currentTime += nonPreemptive;
                    int idx = 0;

                    // check for each second whether to complete on same process or skip to another one
                    while (true)
                    {
                        updateQueue(currentTime, processes);
                        // if there is a ready process with higher AG Factor
                        if(agFactor.get(currProcess) > agFactor.get(getMinProcess()))
                        {
                            // skip to that process instead
                            skipProcess(currProcess, idx, (int)nonPreemptive, false);
                            break;
                        }
                        else
                        {
                            int quanTime = quantumTime.get(currProcess);
                            int remainTime = quanTime - (int)Math.ceil(quanTime/2.0) - idx;
                            int burstTime = burstTimeMp.get(currProcess) - (int)nonPreemptive - idx;

                            // The running process used all its quantum time
                            if(remainTime == 0 && burstTime != 0)
                            {
                                quantumCase = true;
                                skipProcess(currProcess, idx, (int)nonPreemptive, true);
                                break;
                            }

                            // The running process finished all its job
                            else if(burstTime == 0)
                            {
                                quantumCase = true;
                                printQuantum();
                                processes.remove(currProcess);
                                endProcess(currProcess);
                                break;
                            }

                            // remove from ready queue and check which one to add in the next second
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


    public Integer getAgFactor(Process process)
    {
        int agFactor;
        Random random = new Random();
        int randInt = random.nextInt(0, 21);
        if(randInt < 10) agFactor = randInt;
        else if(randInt > 10) agFactor = 10;
        else agFactor = process.getPriorityNumber();
        agFactor = agFactor + process.getArrivalTime() + process.getBurstTime();
        return agFactor;
    }

    public void updateQueue(int currentTime, ArrayList<Process> processes)
    {
        for(int i = 0; i < processes.size(); i++){
            if(processes.get(i).getArrivalTime() <= currentTime && !readyQueue.contains(processes.get(i))){
                readyQueue.add(processes.get(i));
            }
        }
    }

    public void printQuantum()
    {
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

    public void printInitialOutput()
    {
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

    public double meanQuantum()
    {
        double sum = 0;
        int num = quantumTime.size();
        for (var entry: quantumTime.entrySet()) {
            sum += entry.getValue();
        }
        return sum/num;
    }

    public Process getMinProcess()
    {
        PriorityQueue<Process> pq = new PriorityQueue<>(Comparator.comparingInt(l->agFactor.get(l)));
        pq.addAll(readyQueue);
        readyQueue.remove(pq.peek());
        return pq.peek();
    }

    public void endProcess(Process currProcess)
    {
        // reset values in map
        quantumTime.put(currProcess, 0);
        burstTimeMp.put(currProcess, 0);

        // calculating process details
        currProcess.setCompletionTime(currentTime);
        currProcess.setTurnaroundTime(currProcess.getCompletionTime() - currProcess.getArrivalTime());
        currProcess.setWaitingTime(currProcess.getTurnaroundTime() - currProcess.getBurstTime());

        // adding process history
        processHistories.add(new ProcessHistory(currProcess, startTime, currentTime));

        // remove from readyQueue and add to process queue
        readyQueue.remove(currProcess);
        processQueue.add(currProcess);
    }

    public void skipProcess(Process currProcess, int idx, int nonPreemptive, boolean meanQuantum)
    {
        double newQuantum;
        int oldQuantum  = quantumTime.get(currProcess);
        if(meanQuantum)
        {
            // update the quantum time of the old process (10% of mean)
            newQuantum = Math.ceil((1.0 / 10) * meanQuantum());
        }
        else
        {
            // update the quantum time of the old process (add remaining time)
            newQuantum = oldQuantum - (int)Math.ceil(oldQuantum/2.0) - idx;
        }
        quantumTime.put(currProcess, oldQuantum + (int)newQuantum);

        printQuantum();

        // update burst time of old process
        burstTimeMp.put(currProcess, burstTimeMp.get(currProcess) - nonPreemptive - idx);

        // add to process history
        processHistories.add(new ProcessHistory(currProcess, startTime, currentTime));

        // add old process to ready queue
        if(!readyQueue.contains(currProcess)) readyQueue.add(currProcess);
    }
}
