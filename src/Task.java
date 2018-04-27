import java.io.Serializable;

public class Task implements Serializable {
    private int jobTime;
    private String name;
    private String taskStatus = "TASK NOT COMPLETE";//
    private int slaveNumber = -1;

    // constructor is passed the job name and the jobTime (seconds)
    public Task(String name, int jobLengthTime) {
	this.jobTime = jobLengthTime * 1000;
	this.name = name;
    }

    public boolean startTask() {
	try {
	    Thread.sleep(jobTime);
	    taskStatus = "TASK COMPLETE";
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;

    }

    public void setWhichSlaveDidTask(int slaveNumber) {
	this.slaveNumber = slaveNumber;
    }

    public String getTaskStatus() {
	return taskStatus;
    }

    @Override
    public String toString() {
	return "Job name: " + name + "Task Status: " + taskStatus + " -- Time: " + jobTime + "Done by slave number "
		+ slaveNumber;
    }

}
