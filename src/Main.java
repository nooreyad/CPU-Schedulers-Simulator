import org.jfree.ui.RefineryUtilities;
import java.util.*;

public class Main {
    static int id = 1253;
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        System.out.println("CPU Schedulers Simulator");
        System.out.print("Enter number of processes: ");
        int processNum;
        Scanner in = new Scanner(System.in);
        processNum = in.nextInt();
        for(int i = 0; i < processNum; i++){
            System.out.print("Process name: ");
            String processName = in.next();
            System.out.print("Arrival time: ");
            int arrivalTime = in.nextInt();
            System.out.print("Burst time: ");
            int burstTime = in.nextInt();
            System.out.print("Priority number: ");
            int priorityNumber = in.nextInt();
            Process p = new Process(processName, arrivalTime, burstTime, priorityNumber);
            p.setPID(id++);
            processes.add(p);
        }
        ArrayList<Process> tempProcesses = new ArrayList<>(processes);
        Comparator<Process> idComparator = Comparator.comparing(Process::getArrivalTime);
        Collections.sort(processes, idComparator);
        Scheduler scheduler;
        System.out.print("Enter round robin quantum: ");
        int quantum = in.nextInt();
        System.out.print("Enter the overhead of context switching: ");
        int contextSwitching = in.nextInt();
        System.out.println("Choose one of these schedulers:-");
        System.out.println("1. Shortest Job First Scheduler");
        System.out.println("2. Shortest Remaining Time First Scheduler");
        System.out.println("3. Priority Scheduler");
        System.out.println("4. AG Scheduler");
        int choice = in.nextInt();
        switch (choice) {
            case 1 -> scheduler = new SJF(contextSwitching);
            case 2 -> scheduler = new SRTF();
            case 3 -> scheduler = new PriorityScheduler();
            case 4 -> scheduler = new AGScheduler(quantum);
            default -> {
                return;
            }
        }
        scheduler.setExecutionOrder(processes);
        scheduler.displayProcessHistory();
        System.out.println("----------------------------------------");
        System.out.print("\n");
        scheduler.displayProcessesWaitingTime();
        System.out.println("----------------------------------------");
        System.out.print("\n");
        scheduler.displayProcessesTurnaroundTime();
        System.out.println("----------------------------------------");
        System.out.print("\n");
        scheduler.calculateAverageWaitingTime();
        System.out.print("\n");
        scheduler.calculateAverageTurnaroundTime();

        Chart demo = new Chart("Graphical Representation", tempProcesses,  scheduler.getProcessHistories() );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
