import java.nio.DoubleBuffer;
import java.util.*;

public class AGScheduler extends Scheduler{
    private int quantumTime;
    private ArrayList<Process> dieList = new ArrayList<>();
    private Map<Process, Integer> AGFactor = new HashMap<>();
    private Map<Process, ArrayList<Integer>> historyOfQuantumTimeChange = new HashMap<>();
    private PriorityQueue<Process> readyQueue = new PriorityQueue<>(new AGSchedulerPriorityComparator(AGFactor));
    private Map<Process, Integer> QuantumTable = new HashMap<>();
    AGScheduler(ArrayList<Process> processes){
        super(processes);
    }

    private void hist(){
        Map<Map<Integer, Integer>, Process> history = new HashMap<>();
        Map<Integer, Integer> innerMap = new HashMap<>();
        for (Process p : processQueue){
            innerMap.put(p.getArrivalTime(), p.getEntranceTime());
            history.put(innerMap, p);
            innerMap.clear();
        }

    }

    private void initializeHistoryOfQuantumTimeChange(){
        for (Process p : processQueue){
            historyOfQuantumTimeChange.put(p, new ArrayList<>());
        }
    }

    private void calculateAGFactor(){
        for (Process process : processQueue){
            Random random = new Random();
            int randomNum = random.nextInt(21);
            if (randomNum < 10)
                AGFactor.put(process, randomNum + process.getArrivalTime() + process.getBurstTime());
            else if (randomNum == 10)
                AGFactor.put(process, process.getPriorityNumber() + process.getArrivalTime() + process.getBurstTime());
            else
                AGFactor.put(process, 10 + process.getArrivalTime() + process.getBurstTime());
        }
    }

    private void setQuantumTime(){
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter the desired quantum time: ");
        quantumTime = in.nextInt();
        for (Process process : processQueue) {
            QuantumTable.put(process, quantumTime);
        }
    }
    private void updateReadyQueue(int currentTime){
        for (Process process : processQueue) {
            if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process)) {
                readyQueue.add(process);
            }
        }
    }
    private int calculateMenaQuantumTime(){
        int meanQuantumTime = 0;
        for (Map.Entry<Process,Integer> entry : QuantumTable.entrySet()){
            meanQuantumTime += entry.getValue();
        }
        meanQuantumTime /= QuantumTable.size();
        return meanQuantumTime;
    }
    private void updateCase0(Process p){
        Integer newTime = QuantumTable.get(p);
        newTime -= (int) Math.ceil(0.5*QuantumTable.get(p));
        QuantumTable.replace(p, newTime);
        historyOfQuantumTimeChange.get(p).add(newTime);
    }

    private void updateCase2(Process p, int halfTime){
        readyQueue.add(p);
        p.setBurstTime(p.getBurstTime() - halfTime);
        QuantumTable.replace(p, QuantumTable.get(p) + halfTime);
        historyOfQuantumTimeChange.get(p).add(QuantumTable.get(p) + halfTime);
    }
    private void updateCase3(Process p){
        dieList.add(p);
//        QuantumTable.remove(p);
        AGFactor.replace(p, 2147483647);
        historyOfQuantumTimeChange.get(p).add(0);
    }
    private void updateCase1(Process p){
        readyQueue.add(p);
        p.setBurstTime(p.getBurstTime() - QuantumTable.get(p));
        QuantumTable.replace(p, (int) Math.ceil(0.1*calculateMenaQuantumTime()));
        historyOfQuantumTimeChange.get(p).add((int) Math.ceil(0.1*calculateMenaQuantumTime()));
    }

    @Override
    public void setExecutionOrder(ArrayList<Process> processes) {
        initializeHistoryOfQuantumTimeChange();
        calculateAGFactor(); // e7seb ag-factor
        setQuantumTime(); // hat w set quantum time
        int completionTime = processes.stream().mapToInt(Process::getBurstTime).sum();
        int currentTime = 0;
        while (dieList != processQueue){
            // goz2 non-preemptive : hyshtaghal ghasb 3an el kol
            updateReadyQueue(currentTime); // gahez el ready queue
            Process currentlyExecuting = readyQueue.poll(); // hat awel wa7da
            assert currentlyExecuting != null;
            int halfTime =(int)Math.ceil(0.5*QuantumTable.get(currentlyExecuting));
            currentlyExecuting.setBurstTime(currentlyExecuting.getBurstTime() - halfTime);
            currentTime += halfTime; // nos el wa2t 3ada
            updateCase0(currentlyExecuting); // update el quantum time ta3 el current process
            updateReadyQueue(currentTime); // update el ready queue tany ma3lesh
            //// goz2 preemptive : cute hy3ady el asghar meno fel AGFactor

            // case 2 in assign, gat process asghar meny
            if (!readyQueue.isEmpty() && AGFactor.get(readyQueue.peek()) < AGFactor.get(currentlyExecuting)){
                updateCase2(currentlyExecuting, halfTime);
            }
            // case 1 in assign, khalast kol el wa2t bta3y w lesa 3andy job makhlessh
            else if (QuantumTable.get(currentlyExecuting) < currentlyExecuting.getBurstTime()){
                updateCase1(currentlyExecuting);
//                completionTime += (int) Math.ceil(0.1*calculateMenaQuantumTime());
                currentTime += halfTime;
            }
            // case 3 in assign, khalast abl aw wa2t ma el wa2t kheles
            else if (halfTime >= currentlyExecuting.getBurstTime()){
                updateCase3(currentlyExecuting);
                currentTime += currentlyExecuting.getBurstTime();
//                processQueue.remove(currentlyExecuting);
            }
            else{
                System.out.println("yalahwaaaaay");
            }
        }
    }
}
