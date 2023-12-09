import java.util.Comparator;
import java.util.HashMap;

public class AGSchedulerPriorityComparator implements Comparator<Process> {
    public HashMap<Process, Integer> AGFactor;
    public AGSchedulerPriorityComparator(HashMap<Process, Integer> AGFactor){
        this.AGFactor = AGFactor;
    }

    @Override
    public int compare(Process p1, Process p2) {
        ProcessPriorityComparator comparator = new ProcessPriorityComparator();
        if(p1.getArrivalTime() != p2.getArrivalTime()){
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        }
        else{
            return Integer.compare(AGFactor.get(p1), AGFactor.get(p2));
        }
    }
}
