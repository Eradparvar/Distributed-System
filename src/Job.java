
public class Job {
    private int jobLengthTime;

    public Job(int jobLengthTime) {
	this.jobLengthTime = jobLengthTime;
    }
    
    public void start() {
	try {
	    Thread.sleep(jobLengthTime);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	
    }

}
