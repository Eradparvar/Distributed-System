
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveServerProcessTasksThread implements Runnable {

    private ArrayList<Socket> socks; // list of sockets used to return the completed task to MasterServerThread
    private ArrayList<Thread> tasks; // list of tasks to run

    public SlaveServerProcessTasksThread(ArrayList<Socket> socksDir, ArrayList<Thread> tasks) {
	this.socks = socksDir;
	this.tasks = tasks;
    }

    @Override
    public void run() {
	boolean running = true;
	while (running) // if we have no tasks to run, this does nothing
	{
	    if (!tasks.isEmpty()) // if we have a task to run
	    {
		try {
		    Thread t = tasks.get(0); // t is the thread we are going to run next
		    t.run();

		    // used to send the completed task to master
		    Socket s = socks.get(0);
		    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

		    t.join(); // wait until the task has completed

		    // now that the task has completed, we can get rid of it.
		    synchronized (tasks) {
			tasks.remove(0);
			socks.remove(0);
		    }

		    out.writeObject(t); // send the completed task to master
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }
}
