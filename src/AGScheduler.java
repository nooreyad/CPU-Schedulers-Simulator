import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class AGScheduler extends Scheduler{
    private HashMap<Process, Integer> AGFactor = new HashMap<>();
    PriorityQueue<Process> readyQueue = new PriorityQueue<>(new AGSchedulerPriorityComparator(AGFactor));
    private void calculateAGFactor(ArrayList<Process> processes){
        for (Process process : processes){
            Random random = new Random();
            int randomNum = random.nextInt(0, 20);
            if (randomNum < 10)
                AGFactor.put(process, randomNum + process.getArrivalTime() + process.getBurstTime());
            else if (randomNum == 10)
                AGFactor.put(process, process.getPriorityNumber() + process.getArrivalTime() + process.getBurstTime());
            else
                AGFactor.put(process, 10 + process.getArrivalTime() + process.getBurstTime());
        }
    }

    @Override
    public void setExecutionOrder(ArrayList<Process> processes) {
        calculateAGFactor(processes); // e7seb ag-factor
        readyQueue.addAll(processes); // 3aby fel ready queue 3ala 7asab el arrival time(byt3mel automatic)
        int completionTime = processes.stream().mapToInt(Process::getBurstTime).sum();
        int currentTime = 0;
        while (!readyQueue.isEmpty()){
            Process currentlyProcessing = readyQueue.poll(); // hat awel wa7da wasalet

            // goz2 non-preemptive : hyshtaghal ghasb 3an el kol


            // goz2 preemptive : cute hy3ady el asghar meno fel AGFactor
        }
    }
}
