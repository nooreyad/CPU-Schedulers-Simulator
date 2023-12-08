public class Process {
    private String name;

    public int getArrivalTime() {
        return arrivalTime;
    }

    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;

    private int waitingTime;
    private int turnaroundTime;

    Process(String name, int arrivalTime, int burstTime, int priorityNumber){
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
    }

    int calculateWaitingTime(){
        return waitingTime;
    }

    int calculateTurnaroundTime(){
        return turnaroundTime;
    }

   // @Override
//    public int compareTo(Process p) {
//        return Integer.compare(arrivalTime, p.arrivalTime);
//    }
}
