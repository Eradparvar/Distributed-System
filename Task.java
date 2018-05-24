
import java.io.Serializable;

public class Task extends Thread implements Serializable
{
    private int jobTime;
    private String taskName;
    private String taskStatus = "TASK NOT COMPLETE";//
    private int slaveNumber = -1;

    // constructor is passed the job name and the jobTime (seconds)
    public Task(String name, int jobLengthTime)
    {
		this.jobTime = jobLengthTime * 1000;
		this.taskName = name;
    }

    @Override
    public void run()
    {
    	try
    	{
		    System.out.println("Task: " + taskName + " Started");
		    Thread.sleep(jobTime);
		    taskStatus = "TASK COMPLETE";
		    System.out.println("Task: " + taskName + " Complete");
		}
    	catch (InterruptedException e)
    	{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
    }
    
    @Override
    public String toString()
    {
	return "Job name: " + taskName + "Task Status: " + taskStatus + " -- Time: " + jobTime + "Done by slave number "
		+ slaveNumber;
    }

}
