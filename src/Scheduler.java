import java.util.ArrayList;
import java.util.Queue;

public abstract class  Scheduler { //General class representing the cpu scheduler

    ArrayList<Process> processQueue = new ArrayList<>();
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
            System.out.println(p.getName() + " ");
        }
        System.out.print("\n");
    }
    public  void clearExecutionOrder(){
        //needed if there is a main menu and I want to apply another
        //algorithm on the sam set of processes
        processQueue.clear();
    }
    public void displayProcessesWaitingTime(){
        System.out.println("Waiting time for each process:-");
        for(Process p : processQueue){
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }
    }
    public void displayProcessesTurnaroundTime(){
        System.out.println("Turnaround time for each process:-");
        for(Process p : processQueue){
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }
    }
}
