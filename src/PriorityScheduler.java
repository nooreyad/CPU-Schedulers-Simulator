import java.util.*;

public class PriorityScheduler extends Scheduler{
    PriorityQueue<Process> readyQueue;

    @Override
    public void setExecutionOrder(ArrayList<Process> processes) {
        readyQueue = new PriorityQueue<>(new ProcessPriorityComparator());
        boolean solvedStarvation;

        //get the minimum arrival time for the entered processes
        int minArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).min().orElse(Integer.MAX_VALUE);
        int completionTime = processes.stream().mapToInt(Process::getBurstTime).sum() + minArrivalTime;
        int currentTime =  minArrivalTime;

        //switch between processes until all processes are executed
        while(currentTime != completionTime){
            solvedStarvation = false;
            for(int i = 0; i < processes.size(); i++){
                //add all the processes that are suitable for entering the ready queue
                if(processes.get(i).getArrivalTime() <= currentTime && !readyQueue.contains(processes.get(i))){
                    //if the ready queue was not empty, solve the starvation before adding more processes
                    if(!readyQueue.isEmpty() && !solvedStarvation){
                        increasePriority();
                        solvedStarvation = true;
                    }
                    readyQueue.add(processes.get(i));
                }
            }

            //enter the process to the cpu and calculate its timings
            if(!readyQueue.isEmpty()) {
                Process currProcess = readyQueue.poll();
                processes.remove(currProcess);
                currProcess.setEntranceTime(currentTime);
                currProcess.setWaitingTime(currProcess.getEntranceTime() - currProcess.getArrivalTime());
                currProcess.setTurnaroundTime(currProcess.getWaitingTime() + currProcess.getBurstTime());
                processQueue.add(currProcess);
                ProcessHistory processHistory = new ProcessHistory(currProcess, currentTime, currProcess.getBurstTime() + currentTime);
                processHistories.add(processHistory);
                currentTime += currProcess.getBurstTime();
            }

            else{
                //Solving if there is a gap in the cpu without any of the entered processes
                currentTime += 1;
                completionTime += 1;
            }

        }
    }

    //function used to increase the priority to solve the starvation problem
    public void increasePriority(){
        for(Process p : readyQueue) {
            if (p.getPriorityNumber() != 0) {
                int newPriorityNum = p.getPriorityNumber() - 1;
                ;
                p.setPriorityNumber(newPriorityNum);
            }
        }
    }
}
