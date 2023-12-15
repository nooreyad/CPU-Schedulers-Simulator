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

//        ArrayList<Process> processes = new ArrayList<>();
//        processes. add(new Process("P1", 0, 8 , 3));
//        processes .add(new Process("P2", 1, 2 , 4));
//        processes.add(new Process("P3", 3, 4 , 4));
//        processes.add(new Process("P4", 4, 1 , 5));
//        processes.add(new Process("P5", 5, 6 , 2));
//        processes.add(new Process("P6", 6, 5 , 6));
//        processes.add(new Process("P7", 10, 1 , 1));


//        processes. add(new Process("P1", 0, 4 , 3));
//        processes .add(new Process("P2", 1, 3 , 4));
//        processes.add(new Process("P3", 2, 2 , 4));
//        processes.add(new Process("P4", 3, 1 , 5));
        processes.add(new Process("P1",0, 17, 4));
        processes.add(new Process("P2",3, 6, 9));
        processes.add(new Process("P3",4, 10, 3));
        processes.add(new Process("P4",29, 4, 8));
//        scheduler = new AGScheduler(quantum);

        //Testing Priority
//        processes.add(new Process("P1", 0, 10, 1));
//        processes.add(new Process("P2", 1, 2, 3));
//        processes.add(new Process("P3", 2, 2, 1));
//        processes.add(new Process("P4", 3, 2, 1));

        //Testting SRTF
        // processes.add(new Process("P1", 2, 1, 1));
        // processes.add(new Process("P2", 1, 5, 1));
        // processes.add(new Process("P3", 4, 1, 1));
        // processes.add(new Process("P4", 0, 6, 1));
        // processes.add(new Process("P5", 2, 3, 1));

//        Scheduler scheduler = new SRTF();
        scheduler.setExecutionOrder(processes);
        scheduler.displayExecutionOrder();
        scheduler.displayProcessesWaitingTime();
        scheduler.displayProcessesTurnaroundTime();
        scheduler.calculateAverageWaitingTime();
        scheduler.calculateAverageTurnaroundTime();

        Chart demo = new Chart("Graphical Representation", tempProcesses,  scheduler.getProcessHistories() );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
