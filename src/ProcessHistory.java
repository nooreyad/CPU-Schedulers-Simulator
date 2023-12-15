public class ProcessHistory {
    int startTime;
    int endTime;
    Process process;
    public ProcessHistory(Process process, int startTime, int endTime)
    {
        this.process = process;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public Process getProcess() {
        return process;
    }
}
