import java.util.Date;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
public class ChartData extends AbstractXYDataset implements IntervalXYDataset, DatasetChangeListener {
    private TaskSeriesCollection underlying;
    private double yIntervalWidth;
    public ChartData(TaskSeriesCollection tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Null 'tasks' argument.");
        }
        this.underlying = tasks;
        this.yIntervalWidth = 0.8;
        this.underlying.addChangeListener(this);
    }
    public int getSeriesCount() {
        return this.underlying.getSeriesCount();
    }
    public Comparable getSeriesKey(int series) {
        return this.underlying.getSeriesKey(series);
    }
    public int getItemCount(int series) {
        return this.underlying.getSeries(series).getItemCount();
    }
    public Number getX(int series, int item) {
        TaskSeries s = this.underlying.getSeries(series);
        Task t = s.get(item);
        TimePeriod duration = t.getDuration();
        Date start = duration.getStart();
        Date end = duration.getEnd();
        long mid = (start.getTime() / 2L) + (end.getTime() / 2L);
        return Long.valueOf(mid);
    }
    public Number getY(int series, int item) {
        return  Integer.valueOf(series);
    }
    public Number getStartX(int series, int item) {
        return  Double.valueOf(getStartXValue(series, item));
    }
    public double getStartXValue(int series, int item) {
        TaskSeries s = this.underlying.getSeries(series);
        Task t = s.get(item);
        TimePeriod duration = t.getDuration();
        Date start = duration.getStart();
        return start.getTime();
    }
    public Number getEndX(int series, int item) {
        return  Double.valueOf(getEndXValue(series, item));
    }
    public double getEndXValue(int series, int item) {
        TaskSeries s = this.underlying.getSeries(series);
        Task t = s.get(item);
        TimePeriod duration = t.getDuration();
        Date end = duration.getEnd();
        return end.getTime();
    }
    public Number getStartY(int series, int item) {
        return  Double.valueOf(series - this.yIntervalWidth / 2.0);
    }
    public double getStartYValue(int series, int item) {
        return series - this.yIntervalWidth / 2.0;
    }
    public Number getEndY(int series, int item) {
        return  Double.valueOf(series + this.yIntervalWidth / 2.0);
    }
    public double getEndYValue(int series, int item) {
        return series + this.yIntervalWidth / 2.0;
    }
    public void datasetChanged(DatasetChangeEvent event) {
        fireDatasetChanged();
    }

}

