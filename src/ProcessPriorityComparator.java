import java.util.Comparator;

public class ProcessPriorityComparator implements Comparator<Process> {

    @Override
    public int compare(Process p1, Process p2) {
        if(p1.getPriorityNumber() != p2.getPriorityNumber()){
            return Integer.compare(p1.getPriorityNumber(), p2.getPriorityNumber());
        }
        else{
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        }
    }
}
