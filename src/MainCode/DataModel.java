package MainCode;

import java.io.Serializable;

public class DataModel implements Serializable {
    private String activityName;
    private String activityCode;
    private String activityPredecessors;
    private String activityTime;

    public DataModel(){
        this.activityName = "";
        this.activityCode = "";
        this.activityPredecessors = "";
        this.activityTime = "";
    }
    public DataModel(String activityName, String ActivityCode, String precedingTasks, String activityTime){
        this.activityName = activityName;
        this.activityCode = ActivityCode;
        this.activityPredecessors = precedingTasks;
        this.activityTime = activityTime;
    }

    public String getActivityName() { return activityName; }

    public void setActivityName(String activityName) { this.activityName = activityName; }

    public String getActivityCode() { return activityCode; }

    public void setActivityCode(String activityCode) { this.activityCode = activityCode; }

    public String getActivityPredecessors() { return activityPredecessors; }

    public void setActivitypredecessors(String activityPredecessors) { this.activityPredecessors = activityPredecessors; }

    public String getActivityTime() { return activityTime; }

    public void setActivityTime(String activityTime) { this.activityTime = activityTime; }

}
