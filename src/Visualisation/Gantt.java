package Visualisation;

import DataProcessing.ProjectGraph;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;

public class Gantt extends ApplicationFrame{
    private ProjectGraph graph;
    public Gantt(ProjectGraph projectGraph){
        super("Gantt Chart");
        this.graph = projectGraph;
        JPanel jPanel = createPanel();
        jPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jPanel);

        this.pack();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setVisible(true);
    }

    private JPanel createPanel(){
        //TODO
        JFreeChart jf = createChart(createDataset());
        ChartPanel cp = new ChartPanel(jf);
        cp.setMouseWheelEnabled(true);
        return cp;
    }

    private JFreeChart createChart(IntervalCategoryDataset dataset){
        final JFreeChart chart = GanttChartFactory.createGanttChart(
                "Gantt Chart Demo", "Task", "Time Units", dataset, true, true, false);
        return chart;
    }

    private IntervalCategoryDataset createDataset(){
        final TaskSeries min = new TaskSeries("Minimum Time");
        final TaskSeries slack = new TaskSeries("Slack Time");
        for (DataProcessing.Task t : graph.getTaskList() ) {
            min.add(new TaskNumeric(t.getTaskName(), t.getEarliestFinish()-t.getRequiredTime(),t.getEarliestFinish()));
            slack.add(new TaskNumeric(t.getTaskName(), t.getEarliestFinish(),t.getLatestFinish()));
        }

        TaskSeriesCollection tsc = new TaskSeriesCollection();
        tsc.add(min);
        tsc.add(slack);
        return tsc;
    }
}
