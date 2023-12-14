public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;
    private int waitingTime; //(entrance time of the process - process arrival time) + context switching time if there is any
    private int turnaroundTime; //process waiting time + process burst time
    private int entranceTime = -1;
    private int completionTime;

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public String getName() {
        return name;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public void setEntranceTime(int entranceTime) {
        this.entranceTime = entranceTime;
    }
    public int getEntranceTime() {
        return entranceTime;
    }

    Process(String name, int arrivalTime, int burstTime, int priorityNumber){
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
    }

}
