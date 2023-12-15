import java.awt.*;

public class Process {
    private String name;
    private int PID;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;
    private int waitingTime = 0; //(entrance time of the process - process arrival time) + context switching time if there is any
    private int turnaroundTime; //process waiting time + process burst time
    private int entranceTime = -1;
    private int completionTime;
    public String getName() {
        return name;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime){
        this.burstTime = burstTime;
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
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    public int getCompletionTime() {
        return completionTime;
    }
    Process(String name, int arrivalTime, int burstTime, int priorityNumber){
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }
}
