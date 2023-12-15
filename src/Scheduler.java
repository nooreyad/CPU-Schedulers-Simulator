import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//General class representing the cpu scheduler
public abstract class  Scheduler {
    ArrayList<Process> processQueue = new ArrayList<>();
    int completionTime;
    ArrayList<ProcessHistory> processHistories = new ArrayList<>();
    public abstract void setExecutionOrder(ArrayList<Process> processes);

    public ArrayList<ProcessHistory> getProcessHistories() {
        return processHistories;
    }

    public ArrayList<Process> getProcessQueue() {
        return processQueue;
    }

    public void  calculateAverageWaitingTime(){
        double totalWaitingTime = processQueue.stream().mapToInt(Process::getWaitingTime).sum();
        double processesNum = processQueue.size();
        double avgWaitingTime = (totalWaitingTime / processesNum);
        System.out.println("Average waiting time: " + avgWaitingTime);
    }
    public void calculateAverageTurnaroundTime(){
        double totalTurnaroundTime = processQueue.stream().mapToInt(Process::getTurnaroundTime).sum();
        double processesNum = processQueue.size();
        double avgTurnaroundTime = (totalTurnaroundTime / processesNum);
        System.out.println("Average turnaround time: " + avgTurnaroundTime);
    }
    public  void clearExecutionOrder(){
        //needed if there is a main menu and I want to apply another
        //algorithm on the same set of processes
        processQueue.clear();
        processHistories.clear();
    }
    public void displayProcessesWaitingTime(){
        System.out.println("Waiting time for each process:-");
        for(Process p : processQueue){
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }
        System.out.print("\n");
    }

    public void displayProcessesCompletionTime(){
        System.out.println("Completion time for each process:-");
        for(Process p : processQueue){
            System.out.println(p.getName() + ": " + p.getCompletionTime());
        }
        System.out.print("\n");
    }

    public void displayProcessesBurstTime(){
        System.out.println("Burst time for each process:-");
        for(Process p : processQueue){
            System.out.println(p.getName() + ": " + p.getBurstTime());
        }
        System.out.print("\n");
    }
    public void displayProcessesTurnaroundTime(){
        System.out.println("Turnaround time for each process:-");
        for(Process p : processQueue){
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }
        System.out.print("\n");
    }

    public void displayProcessHistory(){
        for (int i = 0; i < processHistories.size(); i++) {
            System.out.println(processHistories.get(i).getProcess().getName());
            System.out.println("Start Time: " + processHistories.get(i).getStartTime());
            System.out.println("End Time: " + processHistories.get(i).getEndTime());
            System.out.println();
        }

    }
}
