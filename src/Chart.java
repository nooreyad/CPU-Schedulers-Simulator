import java.awt.EventQueue;
import java.text.DateFormat;
import java.util.*;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;


public class Chart {
    ArrayList<ProcessHistory> processHistories;
    ArrayList<Process> processes;
    int minArrivalTime = 0;
    int completionTime = 0;


    class MyToolTipGenerator extends IntervalCategoryToolTipGenerator {

        DateFormat format;

        private MyToolTipGenerator(String value, DateFormat format) {
            super(value, format);
            this.format = format;
        }

        @Override
        public String generateToolTip(CategoryDataset cds, int row, int col) {
            final String s = super.generateToolTip(cds, row, col);
            TaskSeriesCollection tsc = (TaskSeriesCollection) cds;
            StringBuilder sb = new StringBuilder(s);
            for (int i = 0; i < tsc.getSubIntervalCount(row, col); i++) {
                sb.append(format.format(tsc.getStartValue(row, col, i)));
                sb.append("-");
                sb.append(format.format(tsc.getEndValue(row, col, i)));
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }
    Chart(ArrayList<Process> processes, ArrayList<ProcessHistory> processHistories){
        this.processes = processes;
        this.processHistories = processHistories;
    }
    private JFreeChart createChart() {
        IntervalCategoryDataset xyDataset = createDataset();
        JFreeChart jFreeChart = ChartFactory.createGanttChart("CPU Schedulers Simulator",
                "Process", "Time", xyDataset, true, true, true);
        CategoryPlot plot = jFreeChart.getCategoryPlot();
        plot.getRenderer().setBaseToolTipGenerator( new MyToolTipGenerator("{0}, {1} ", DateFormat.getTimeInstance(DateFormat.SHORT)));
        return jFreeChart;
    }

    private IntervalCategoryDataset createDataset() {
        System.out.println(processes.size());
        minArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).min().orElse(Integer.MAX_VALUE);
        completionTime = processes.stream().mapToInt(Process::getBurstTime).sum() + minArrivalTime;
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        Map<Process, Task> tasks = new HashMap<>();
        for (int i = 0; i < processes.size(); i++) {
            boolean flag = false;
            String name = processes.get(i).getName();
            for (int j = 0; j < processHistories.size(); j++) {
                if(processes.get(i).equals(processHistories.get(j).getProcess())){
                    if(!flag){
                        tasks.put(processes.get(i), new Task(name, date(0), date(completionTime)));
                        tasks.get(processes.get(i)).addSubtask(new Task(name, date(processHistories.get(i).getStartTime()), date(processHistories.get(i).getEndTime())));
                        flag = true;
                    }
                    else {
                        tasks.get(processes.get(i)).addSubtask(new Task(name, date(processHistories.get(i).getStartTime()), date(processHistories.get(i).getEndTime())));
                    }
                }
            }
        }

//        ArrayList<TaskSeries> taskSeries = new ArrayList<>()
//        for (var entry: tasks.entrySet()) {
//            TaskSeries taskSeries = new TaskSeries(entry.getKey().getName());
//            taskSeries.add(entry.getValue());
//            dataset.add(taskSeries);
//        }
        // dataset --> taskSeries --> task --> subtasks


//        TaskSeries unavailable = new TaskSeries("Process1");
//        Task t1 = new Task("Meeting Room 1", date(0), date(completionTime));
//        System.out.println(completionTime);
//        t1.addSubtask(new Task("Meeting 1", date(9), date(16)));
//        unavailable.add(t1);
//
//        Task t2 = new Task("Meeting Room 2", date(0), date(20));
//        t2.addSubtask(new Task("Meeting 4", date(10), date(11)));
//        t2.addSubtask(new Task("Meeting 5", date(13), date(15)));
//        t2.addSubtask(new Task("Meeting 6", date(16), date(18)));
//        unavailable.add(t2);
//        dataset.add(unavailable);
        return dataset;
    }

    private Date date(int seconds) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2009, Calendar.DECEMBER, 0, 0, 0, seconds);
        return calendar.getTime();
    }

    public void display() {
        JFrame f = new JFrame("Graphical Representation");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new ChartPanel(createChart()));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}