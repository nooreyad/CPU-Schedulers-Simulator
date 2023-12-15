import java.awt.*;
import java.util.*;

public class SRTF extends Scheduler {

    PriorityQueue<Process> readyQueue;
    Map<Process, Integer> originalBurstTime;
    ArrayList<Process> processesExecution = new ArrayList<>();

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
                currProcess = readyQueue.peek();
                currProcess.setBurstTime(readyQueue.peek().getBurstTime() - 1);
                currentTime += 1;

                //if the processes completed its burst time, remove it
                if (readyQueue.peek().getBurstTime() == 0) {
                    currProcess.setCompletionTime(currentTime);
                    processQueue.add(currProcess);
                    processes.remove(readyQueue.poll());
                }
                processesExecution.add(currProcess);

                //set the process waiting time to zero if it took a chance to execute
                currProcess.setWaitingTime(0);

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
                        if (processList.get(i).getWaitingTime() == maxWaitingTime) {
                            currProcess = processList.get(i);
                            currProcess.setBurstTime(currProcess.getBurstTime() - 1);
                            currentTime += 1;
                            if (currProcess.getBurstTime() == 0) {
                                currProcess.setCompletionTime(currentTime);
                                readyQueue.remove(currProcess);
                                processQueue.add(currProcess);
                                processList.remove(currProcess);
                            }
                            currProcess.setWaitingTime(0);
                            processesExecution.add(currProcess);

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
                //Solving if there is a gap in the cpu without any of the entered processes
                currentTime += 1;
                completionTime += 1;
            }
        }
        setHistory(minArrivalTime);
        clearStarvationWaitingTime();
        setTimings();
    }

    //set history of processes' execution
    public void setHistory(int minArrivalTime){
        int startTime = minArrivalTime;
        int endTime = startTime + 1;
        for(int i = 0; i < processesExecution.size(); i++){
            Process p = processesExecution.get(i);
            for(int j = i + 1; j < processesExecution.size(); j++){
                if(processesExecution.get(j) != p){
                    ProcessHistory processHistory = new ProcessHistory(p, startTime, endTime);
                    processHistories.add(processHistory);
                    startTime = endTime;
                    endTime++;
                    i = j - 1;
                    break;
                }
                else if(j == processesExecution.size() - 1){
                    ProcessHistory processHistory = new ProcessHistory(p, startTime, endTime + 1);
                    processHistories.add(processHistory);
                    i = j;
                }
                endTime++;
            }
        }
    }

    //clear waiting time for all processes to zero
    public void clearStarvationWaitingTime(){
        for (Process p : processQueue){
            p.setWaitingTime(0);
        }
    }
    //set waiting and turnaround time for the processes
    public void setTimings(){
        for (Process p : processQueue) {
            p.setTurnaroundTime(p.getCompletionTime() - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - originalBurstTime.get(p));
        }
    }
}
