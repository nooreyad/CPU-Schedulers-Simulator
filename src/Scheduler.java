import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

//General class representing the cpu scheduler
public abstract class  Scheduler {
    //TODO make a data structure containing the original arrangement of the input processes
    ArrayList<Process> processQueue = new ArrayList<>();
    ArrayList<ProcessHistory> processHistories = new ArrayList<>();
    public abstract void setExecutionOrder(ArrayList<Process> processes);

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
    public  void displayExecutionOrder(){
        System.out.println("Processes' execution order:-");
        for(Process p : processQueue){
            System.out.print(p.getName() + " ");
        }
        System.out.print("\n");
    }
    public  void clearExecutionOrder(){
        //needed if there is a main menu and I want to apply another
        //algorithm on the sam set of processes
        processQueue.clear();
    }
    public void displayProcessesWaitingTime(){
        Map<Process, Boolean> visited = new HashMap<>();
        System.out.println("Waiting time for each process:-");
        for(Process p : processQueue){
            if(!visited.containsKey(p) || !visited.get(p)) {
                System.out.println(p.getName() + ": " + p.getWaitingTime());
            }
            visited.put(p, true);
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
        Map<Process, Boolean> visited = new HashMap<>();
        System.out.println("Turnaround time for each process:-");
        for(Process p : processQueue){
            if(!visited.containsKey(p) || !visited.get(p)) {
                System.out.println(p.getName() + ": " + p.getTurnaroundTime());
            }
            visited.put(p, true);
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
