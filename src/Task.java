import java.io.Serializable;

public class Task implements Serializable {
    private int jobTime;
    private String name;

    //constructor is passed the job name and the jobTime (seconds) 
    public Task(String name, int jobLengthTime) {
	this.jobTime = jobLengthTime*1000;
	this.name = name;
    }

    public boolean start() {
	try {
	    Thread.sleep(jobTime);
	    return true;
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;

    }

    @Override
    public String toString() {
	return "Job name: " + name + " -- Time: " + jobTime;
    }

}
