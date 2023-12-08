import java.util.*;

public class Main {
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
            processes.add(p);
        }
        Comparator<Process> idComparator = Comparator.comparing(Process::getArrivalTime);
        Collections.sort(processes, idComparator);
        Scheduler scheduler;
//        for(Process p : processes){
//            System.out.print(p.getArrivalTime() + " ");
//        }
        System.out.print("Enter round robin quantum: ");
        int quantum = in.nextInt();
        System.out.print("Enter the overhead of context switching: ");
        int contextSwitching = in.nextInt();
        System.out.println("Choose one of these schedulers:-");
        System.out.println("1. Shortest Job First Scheduler");
        System.out.println("2. Shortest Remaining Time First Scheduler");
        System.out.println("3. Priority Scheduler");
        int choice = in.nextInt();
        switch (choice){
            case 1:
                scheduler = new SJF();
                break;
            case 2:
                scheduler = new SRTF();
                break;
            case 3:
                scheduler = new PriorityScheduler();
                break;
        }
    }
}
