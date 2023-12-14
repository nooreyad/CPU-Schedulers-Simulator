import java.util.Comparator;

public class SRTFComparator implements Comparator<Process> {
    @Override
    public int compare(Process p1, Process p2) {
        if(p1.getBurstTime() != p2.getBurstTime()){
            return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
        }
        else{
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        }
    }
}
