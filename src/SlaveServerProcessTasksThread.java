
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class SlaveServerProcessTasksThread implements Runnable
{

    private ArrayList<Socket> socks;
    private ArrayList<Thread> threads;

    public SlaveServerProcessTasksThread(ArrayList<Socket> socks, ArrayList<Thread> threads)
    {
    	this.socks = socks;
    	this.threads = threads;
    }

    @Override
    public void run() 
    {
		boolean running = true;
		while (running) 
		{
		    while (!threads.isEmpty()) 
		    {
				try 
				{
					Thread t = threads.get(0);
				    t.run();
				    
					Socket s = socks.get(0);
				    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				    
				    t.join();
				    synchronized(threads)
				    {
				    	threads.remove(0);
				    }
				    synchronized(socks)
				    {
				    	socks.remove(0);
				    }
				    
				    // send Task completion status to client
				    out.writeObject(t);
				} 
				catch (IOException e) 
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
    }
}
