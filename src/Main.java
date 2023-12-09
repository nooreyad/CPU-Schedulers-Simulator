import java.util.*;

public class Main {
    public static void main(String[] args) {
//        ArrayList<Process> processes = new ArrayList<>();
//        System.out.println("CPU Schedulers Simulator");
//        System.out.print("Enter number of processes: ");
//        int processNum;
//        Scanner in = new Scanner(System.in);
//        processNum = in.nextInt();
//        for(int i = 0; i < processNum; i++){
//            System.out.print("Process name: ");
//            String processName = in.next();
//            System.out.print("Arrival time: ");
//            int arrivalTime = in.nextInt();
//            System.out.print("Burst time: ");
//            int burstTime = in.nextInt();
//            System.out.print("Priority number: ");
//            int priorityNumber = in.nextInt();
//            Process p = new Process(processName, arrivalTime, burstTime, priorityNumber);
//            processes.add(p);
//        }
//        Comparator<Process> idComparator = Comparator.comparing(Process::getArrivalTime);
//        Collections.sort(processes, idComparator);
//        Scheduler scheduler;
////        for(Process p : processes){
////            System.out.print(p.getArrivalTime() + " ");
////        }
//        System.out.print("Enter round robin quantum: ");
//        int quantum = in.nextInt();
//        System.out.print("Enter the overhead of context switching: ");
//        int contextSwitching = in.nextInt();
//        System.out.println("Choose one of these schedulers:-");
//        System.out.println("1. Shortest Job First Scheduler");
//        System.out.println("2. Shortest Remaining Time First Scheduler");
//        System.out.println("3. Priority Scheduler");
//        int choice = in.nextInt();
//        switch (choice){
//            case 1:
//                scheduler = new SJF();
//                break;
//            case 2:
//                scheduler = new SRTF();
//                break;
//            case 3:
//                scheduler = new PriorityScheduler();
//
//                break;
//        }

        ArrayList<Process> processes = new ArrayList<>();
        processes. add(new Process("P1", 0, 8 , 3));
        processes .add(new Process("P2", 1, 2 , 4));
        processes.add(new Process("P3", 3, 4 , 4));
        processes.add(new Process("P4", 4, 1 , 5));
        processes.add(new Process("P5", 5, 6 , 2));
        processes.add(new Process("P6", 6, 5 , 6));
        processes.add(new Process("P7", 10, 1 , 1));


//        processes.add(new Process("P1", 0, 1, 1));
//        processes.add(new Process("P2", 1, 2, 3));
//        processes.add(new Process("P3", 1, 2, 1));
//        processes.add(new Process("P4", 3, 2, 1));
//        processes.add(new Process("P5", 3, 2, 1));

        Scheduler scheduler = new PriorityScheduler();
        scheduler.setExecutionOrder(processes);
        scheduler.displayExecutionOrder();
        scheduler.displayProcessesWaitingTime();
        scheduler.displayProcessesTurnaroundTime();
        scheduler.calculateAverageWaitingTime();
        scheduler.calculateAverageTurnaroundTime();
    }
}
