import java.util.*;

public class SJF extends Scheduler {

    private int CONTEXT_SWITCH_TIME;
    SJF(int contextSwitching){
        this.CONTEXT_SWITCH_TIME = contextSwitching;
    }
    @Override
    public void setExecutionOrder(ArrayList<Process> processes) {
        int n = processes.size();

        //val = 1 if the process is completed
        Map<Process,Boolean> mp = new HashMap<>();
        for (Process p : processes) {
            mp.put(p,false);
        }
        int currentTime =  0;

        int totalProcesses = 0; // initially

        while (totalProcesses < processes.size()) {
            int cnt = n;
            int mn = Integer.MAX_VALUE;

            for(int i = 0 ; i < processes.size();i++) {
                if ((processes.get(i).getArrivalTime() <= currentTime) && (mp.get(processes.get(i)) == false) && (processes.get(i).getBurstTime() < mn)) {
                    mn = processes.get(i).getBurstTime();
                    cnt = i; //process i turn
                }
            }
            if(cnt == n){ //no processes at the current time
                currentTime++;
            }
            else{
                Process currentProcess = processes.get(cnt);
                currentTime += CONTEXT_SWITCH_TIME;

                // Set waiting time and turnaround time
                currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setTurnaroundTime(currentProcess.getWaitingTime() + currentProcess.getBurstTime());
                ProcessHistory processHistory = new ProcessHistory(currentProcess,currentTime, currentTime + currentProcess.getBurstTime());
                processHistories.add(processHistory);
                currentTime += currentProcess.getBurstTime() ;
                processQueue.add(currentProcess);
                mp.put(currentProcess, true);
                totalProcesses++;
            }
        }
    }
}



