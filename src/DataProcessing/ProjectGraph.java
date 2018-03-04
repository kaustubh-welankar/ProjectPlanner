package DataProcessing;

import MainCode.DataModel;
import Visualisation.CPM;
import Visualisation.Gantt;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ProjectGraph {
    private Task dummyBegin;
    private Task dummyEnd;

    private ArrayList<Task> taskList;

    //TODO Validate if cycles are not present
    public ProjectGraph(final ObservableList<DataModel> observableDataList) throws Exception{
        taskList = new ArrayList<>();
        dummyBegin = new Task("Dummy Begin", -1, 0);
        dummyEnd = new Task("Dummy End", -2, 0);

        HashMap<Integer, Task> hashMap = new HashMap<>();

        //---Creating TaskList
        for (DataModel d:observableDataList ) {
            int num = Integer.parseInt(d.getActivityCode());
            int time = Integer.parseInt(d.getActivityTime());
            Task t = new Task(d.getActivityName(),num,time);
            hashMap.put(num,t);
            taskList.add(t);
        }

        for(DataModel d:observableDataList) {
            int num = Integer.parseInt(d.getActivityCode());

            Task t = hashMap.get(num);

            StringTokenizer st = new StringTokenizer(d.getActivityPredecessors());
            while (st.hasMoreTokens()){
                int i = Integer.parseInt(st.nextToken());
                if(hashMap.containsKey(i)){
                    t.addDependency(hashMap.get(i));
                    hashMap.get(i).addNext(t);
                }
                else {
                    throw new Exception();
                }
            }
        }

        for (Task t: this.taskList) {
            if(t.getDependencies().isEmpty()){
                t.getDependencies().add(dummyBegin);
                dummyBegin.addNext(t);
            }
            if(t.getNextTasks().isEmpty()){
                t.getNextTasks().add(dummyEnd);
                dummyEnd.addDependency(t);
            }
        }

        generateGraph();
        new CPM(this);
        new Gantt(this);
        for (Task t: taskList ) {
            System.out.println(t.getIdentifier() + " EF : " + t.getEarliestFinish() + " LF " + t.getLatestFinish());
        }
    }

    private void resetGraph(){
        for (Task t : taskList) {
            t.resetFinishData();
        }
        dummyEnd.resetFinishData();
        dummyBegin.resetFinishData();
    }

    public void generateGraph(){
        resetGraph();
        calculateEarliestFinish(dummyEnd);
        calculateLatestFinish(dummyBegin);
    }

    private void calculateEarliestFinish(Task endTask){
        if(endTask == dummyBegin){
            dummyBegin.setEarliestFinish(0);
            return ;
        }
        ArrayList<Task> tasks = endTask.getDependencies();
        for (Task t : tasks ) {
            if(t.getEarliestFinish() == -1){
                calculateEarliestFinish(t);
            }
        }
        int maxTime = tasks.get(0).getEarliestFinish();
        for (Task t : tasks ) {
            if(t.getEarliestFinish() > maxTime){
                maxTime = t.getEarliestFinish();
            }
        }
        endTask.setEarliestFinish(maxTime+endTask.getRequiredTime());
    }

    private void calculateLatestFinish(Task beginTask){
        if(beginTask == dummyEnd){
            beginTask.setLatestFinish(beginTask.getEarliestFinish());
            return;
        }
        ArrayList<Task> taskArrayList = beginTask.getNextTasks();

        for (Task t: taskArrayList ) {
            if(t.getLatestFinish() == -1){
                calculateLatestFinish(t);
            }
        }

        int minLatestTime = taskArrayList.get(0).getLatestFinish() - taskArrayList.get(0).getRequiredTime();
        for(Task t: taskArrayList ) {
            if(minLatestTime > t.getLatestFinish() - t.getRequiredTime() ){
                minLatestTime = t.getLatestFinish() - t.getRequiredTime();
            }
        }
        beginTask.setLatestFinish(minLatestTime);
    }

    //---Getters---
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public Task getDummyBegin() {
        return dummyBegin;
    }

    public Task getDummyEnd() {
        return dummyEnd;
    }


}
