package Visualisation;

import DataProcessing.ProjectGraph;
import DataProcessing.Task;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.ArrayList;


public class CPM {
    public CPM(ProjectGraph projectGraph){
        System.setProperty("org.graphstream.ui", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        Graph g = new SingleGraph("New Graph");
        ArrayList<Task> tasks= projectGraph.getTaskList();
        Task dummyBegin = projectGraph.getDummyBegin();
        Task dummyEnd = projectGraph.getDummyEnd();

        for(Task t : tasks){
            String l = Integer.toString(t.getIdentifier());
            g.addNode(l);
            Node n = g.getNode(l);

            if(t.isCritical()) {
                n.setAttribute("ui.style", "shape:circle;fill-color:red;size:30px;text-alignment:center;text-size:15px;");
            }
            else{
                n.setAttribute("ui.style", "shape:circle;size:30px;text-alignment:center;text-size:15px;");
            }
            if(t.getDependencies().contains(dummyBegin)){
                n.setAttribute("ui.style", "shape:circle;fill-color:blue;size:30px;text-alignment:center;text-size:15px;");
            }
            if(t.getNextTasks().contains(dummyEnd)){
                n.setAttribute("ui.style", "shape:circle;fill-color:blue;size:30px;text-alignment:center;text-size:15px;");
            }
            n.setAttribute("ui.label", Integer.toString(t.getIdentifier()));
        }

        for (Task t: tasks ) {
            ArrayList<Task> nextNodes = t.getNextTasks();
            for (Task nt : nextNodes ) {
                if(nt != dummyEnd) {
                    boolean crit = t.isCritical() && nt.isCritical() &&
                            (t.getEarliestFinish()+nt.getRequiredTime() == nt.getEarliestFinish());
                    String l = Integer.toString(t.getIdentifier()) + " " + Integer.toString(nt.getIdentifier());
                    g.addEdge(l, Integer.toString(t.getIdentifier()), Integer.toString(nt.getIdentifier()),true);
                    Edge e = g.getEdge(l);
                    if (crit) {
                        e.setAttribute("ui.style", "fill-color:red;size:5px;arrow-size:10px,10px;");
                    } else {
                        e.setAttribute("ui.style", "fill-color:black;size:5px;arrow-size:10px,10px;");
                    }
                }
            }

        }

        Viewer v = g.display();
        v.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

    }
}
