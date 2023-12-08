import java.util.ArrayList;
import java.util.Queue;

public abstract class Scheduler {
    Queue<Process> processes;
    ArrayList<Process> processesExecutionOrder;
    int contextSwitching;
    public abstract double calculateAverageWaitingTime();
    public abstract double calculateAverageTurnaroundTime();
    public void displayExecutionOrder(){
        for(Process p : processesExecutionOrder){
            System.out.print(p + " ");
        }
    }
}
