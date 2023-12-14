import java.util.*;

public class SRTF extends Scheduler {

    PriorityQueue<Process> readyQueue;
    Map<Process, Integer> originalBurstTime;
    final int maxWaitingTime = 4;

    //save the original burst time for the processes
    public void setOriginalBurstTime(ArrayList<Process> processes){
        originalBurstTime = new HashMap<>();
        for(Process p : processes){
            originalBurstTime.put(p, p.getBurstTime());
        }
    }
    @Override
    public void setExecutionOrder(ArrayList<Process> processes) {
        readyQueue = new PriorityQueue<>(new SRTFComparator());
        setOriginalBurstTime(processes);
        //get the minimum arrival time
        int minArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).min().orElse(Integer.MAX_VALUE);
        int completionTime = processes.stream().mapToInt(Process::getBurstTime).sum() + minArrivalTime;
        int currentTime =  minArrivalTime;
        ArrayList<Process> tempProcessQueue = new ArrayList<>();
        Process currProcess;
        while(currentTime != completionTime){
            //add all the processes that are suitable for entering the ready queue
            for(int i = 0; i < processes.size(); i++){
                if(processes.get(i).getArrivalTime() <= currentTime && !readyQueue.contains(processes.get(i))){
                    readyQueue.add(processes.get(i));
                }
            }
            if(!readyQueue.isEmpty()) {
                //make the lowest remaining time process execute for one second
                //boolean finished = false;
                currProcess = readyQueue.peek();
                currProcess.setBurstTime(readyQueue.peek().getBurstTime() - 1);
                currentTime += 1;
                //if the processes completed its burst time, remove it
                if (readyQueue.peek().getBurstTime() == 0) {
                    //finished = true;
                    currProcess.setCompletionTime(currentTime);
                    processes.remove(readyQueue.poll());
                }
                //set the process waiting time to zero if it took a chance to execute
                currProcess.setWaitingTime(0);
                tempProcessQueue.add(currProcess);
                //solve the starvation problem using maximum limit for waiting time
                ArrayList<Process> processList = new ArrayList<>(readyQueue);
                if(!processList.isEmpty()) {
                    //increment the waiting time of the remaining processes in the ready queue with 1 unit
                    for (Process p : processList) {
                        if (p != currProcess) {
                            p.setWaitingTime(p.getWaitingTime() + 1);
                        }
                    }
                    for (int i = 0; i < processList.size(); i++) {
                        //if a process' waiting time reached its max limit, let it enter the cpu
                        if (processList.get(i).getWaitingTime() == maxWaitingTime && processList.get(i) != currProcess) {
                            currProcess = processList.get(i);
                            currProcess.setBurstTime(currProcess.getBurstTime() - 1);
                            currentTime += 1;
                            if (currProcess.getBurstTime() == 0) {
                                currProcess.setCompletionTime(currentTime);
                                readyQueue.remove(currProcess);
                                processList.remove(currProcess);
                            }
                            currProcess.setWaitingTime(0);
                            tempProcessQueue.add(currProcess);
                            //increase waiting time of the processes that were in the ready queue with 1 except the recently executed process
                            for (int j = 0; j < processList.size(); j++) {
                                if (processList.get(j) != currProcess) {
                                    processList.get(j).setWaitingTime(processList.get(j).getWaitingTime() + 1);
                                }
                            }
                            i = -1;
                        }
                    }
                }
            }
            else{
                currentTime += 1;
                completionTime += 1;
            }
        }
        //clear duplications of processes in the process queue
        for(int i = 0; i < tempProcessQueue.size(); i++){
            if(processQueue.isEmpty()){
                processQueue.add(tempProcessQueue.get(i));
            }
            else if(processQueue.get(processQueue.size() - 1) != tempProcessQueue.get(i)){
                processQueue.add(tempProcessQueue.get(i));
            }
        }
        clearStarvationWaitingTime();
        setTimings();
    }
    //clear waiting time for all processes to zero
    public void clearStarvationWaitingTime(){
        for (Process p : processQueue){
            p.setWaitingTime(0);
        }
    }
    //set the waiting and turnaround time for the processes
    public void setTimings(){
        for (Process p : processQueue) {
            p.setTurnaroundTime(p.getCompletionTime() - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - originalBurstTime.get(p));
        }
    }
}
