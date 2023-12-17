import java.util.*;

public class PriorityScheduler extends Scheduler{
    PriorityQueue<Process> readyQueue = new PriorityQueue<>(new ProcessPriorityComparator());
    PriorityScheduler(ArrayList<Process> processes){
        super(processes);
    }

    @Override
    public void setExecutionOrder(ArrayList<Process> processes) {
        int completionTime = processes.stream().mapToInt(Process::getBurstTime).sum();
        int currentTime = 0;
        while(currentTime != completionTime){
            for(int i = 0; i < processes.size(); i++){
                if(processes.get(i).getArrivalTime() <= currentTime && !readyQueue.contains(processes.get(i))){
                    readyQueue.add(processes.get(i));
                }
            }
            if(!readyQueue.isEmpty()) {
                Process currProcess = readyQueue.poll();
                processes.remove(currProcess);
                currProcess.setEntranceTime(currentTime);
                currProcess.setWaitingTime(currProcess.getEntranceTime() - currProcess.getArrivalTime());
                currProcess.setTurnaroundTime(currProcess.getWaitingTime() + currProcess.getBurstTime());
                processQueue.add(currProcess);
                currentTime += currProcess.getBurstTime();
                increasePriority();
            }
            else{
                return;
            }
        }
    }
    public void increasePriority(){
        for(Process p : readyQueue){
            int newPriorityNum = p.getPriorityNumber() - 1;;
            p.setPriorityNumber(newPriorityNum);
        }
    }
}
