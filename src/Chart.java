import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;

public class Chart extends ApplicationFrame {
    ArrayList<ProcessHistory> processHistories;
    ArrayList<Process> processes;
    JFrame frame;
    JTable table;
    int minArrivalTime = 0;
    int completionTime = 0;
    public Chart(String title, ArrayList<Process> processes, ArrayList<ProcessHistory> processHistories) {
        super(title);
        this.processes = processes;
        this.processHistories = processHistories;
        JPanel chartPanel = createPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 300));
        setContentPane(chartPanel);
        JTableImplementation();
    }

    public void JTableImplementation(){
        frame = new JFrame();
        frame.setTitle("Process Information");
        final int cols = processes.size();
        String[][] data = new String[4][cols];
        String[] columnNames = {"Process", "Name", "PID", "Priority"};

        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        table = new JTable(model);

        for (int j = 0; j < processes.size(); j++) {
            Process p = processes.get(j);
            Object[] o = new Object[4];
            o[0] = Integer.toString(j);
            o[1] = p.getName();
            o[2] = Integer.toString(p.getPID());
            o[3] = Integer.toString(p.getPriorityNumber());
            model.insertRow(j, o);
        }

        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        frame.setSize(500, 200);
        frame.setVisible(true);
    }

    private JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart("CPU Schedulers Simulator",
                "Time", true, "Processes", dataset, PlotOrientation.VERTICAL,
                true, false, false);

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRangeAxis(new DateAxis("Time"));
        String[] values = new String[processes.size()];
        for (int i = 0; i < processes.size(); i++) {
            values[i] = processes.get(i).getName();
        }
        SymbolAxis yAxis = new SymbolAxis("Processes", values);
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1));
        axis.setDateFormatOverride(new SimpleDateFormat("ss"));
        yAxis.setGridBandsVisible(false);
        plot.setRangeAxis(yAxis);
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setUseYInterval(true);
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        return chart;
    }
    public JPanel createPanel(){
        return new ChartPanel(createChart(createDataset()));
    }

    private IntervalXYDataset createDataset() {
        return new ChartData(createTasks());
    }

    private Map<Integer,Integer> getStartTime(Process currProcess){
        Map<Integer, Integer> mp = new HashMap<>();
        for (int j = 0; j < processHistories.size(); j++) {
                if(currProcess.equals(processHistories.get(j).getProcess())){
                    mp.put(processHistories.get(j).getStartTime(), processHistories.get(j).getEndTime());
                    processHistories.remove(processHistories.get(j));
                    return mp;
                }
            }
        return null;
    }

    private TaskSeriesCollection createTasks() {
        minArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).min().orElse(Integer.MAX_VALUE);
        completionTime = processes.stream().mapToInt(Process::getBurstTime).sum() + minArrivalTime;

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        for (int i = 0; i < processes.size(); i++) {
            TaskSeries s1 = new TaskSeries(processes.get(i).getName());
            Map<Integer, Integer> mp;
            mp = getStartTime(processes.get(i));
            while(mp != null){
                int x = 0, y = 0;
                for (var entry: mp.entrySet()) {
                    x = entry.getKey();
                    y = entry.getValue();
                }
                while(x != y){
                    SimpleDateFormat originalFormat = new SimpleDateFormat("ss");
                    Date date;
                    try {
                        date = originalFormat.parse(Integer.toString(x));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    s1.add(new Task("duration", new Second(date)));
                    x++;
                }
                mp = getStartTime(processes.get(i));
            }
            dataset.add(s1);
        }
        return dataset;
    }
}