import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AGSchedulerPriorityComparator implements Comparator<Process> {
    public Map<Process, Integer> AGFactor;
    public AGSchedulerPriorityComparator(Map<Process, Integer> AGFactor){
        this.AGFactor = AGFactor;
    }

    @Override
    public int compare(Process p1, Process p2) {
        if(p1.getArrivalTime() != p2.getArrivalTime()){
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        }
        else{
            return Integer.compare(AGFactor.get(p1), AGFactor.get(p2));
        }
    }
}
