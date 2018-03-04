package DataProcessing;

import java.util.ArrayList;

public class Task {
    private String taskName;
    private int identifier;
    private ArrayList<Task> dependencies;
    private ArrayList<Task> nextTasks;
    private int requiredTime;
    private int earliestFinish;
    private int latestFinish;

    /**
     * This function only creates the Task object without dependencies.
     * The dependencies have to added later, after all Tasks are created
     *
     * @param Identifier Integer Identifier for node.
     *                   It will be used instead of the string Identifier
     * @param TaskName The name of the task. A string.
     *                 Will be used in the Gantt Chart
     * @param requiredTime The time required for the task after all dependencies are completed
     */
    public Task(String TaskName, int Identifier, int requiredTime){
        this.taskName = TaskName;
        this.identifier = Identifier;
        this.requiredTime = requiredTime;
        this.dependencies = new ArrayList<>();
        this.nextTasks = new ArrayList<>();
        this.resetFinishData();
    }

    /**
     * Resets finish time data. Needed when recomputing the graph and chart
     */
    protected void resetFinishData(){
        earliestFinish = -1;
        latestFinish = -1;
    }

    public void addDependency(Task t){
        dependencies.add(t);
    }

    public void addNext(Task t){
        nextTasks.add(t);
    }

    public boolean isCritical(){
        return earliestFinish == latestFinish;
    }

    //--Getters and Setters;
    public ArrayList<Task> getDependencies() {
        return dependencies;
    }

    public ArrayList<Task> getNextTasks() {
        return nextTasks;
    }

    public int getRequiredTime() {
        return requiredTime;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getEarliestFinish() {
        return earliestFinish;
    }

    public void setEarliestFinish(int earliestFinish) {
        this.earliestFinish = earliestFinish;
    }

    public int getLatestFinish() {
        return latestFinish;
    }

    public void setLatestFinish(int latestFinish) {
        this.latestFinish = latestFinish;
    }

    public String getTaskName() {
        return taskName;
    }
}
