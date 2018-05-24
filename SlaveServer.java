
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveServer
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {

	    // Hardcode in Port here if required
	    args = new String[] { "102"};

	    if (args.length != 1)
	    {
			System.err.println("Usage: java SlaveServer <port number>");
			System.exit(1);
	    }
	    
		try(ServerSocket serverSocketForTask = new ServerSocket(Integer.parseInt(args[0]));)
		{
		    ArrayList<Socket> socks = new ArrayList<>();
		    ArrayList<Thread> tasks = new ArrayList<>();
		    
		    
		    Thread runtasks = new Thread(new SlaveServerProcessTasksThread(socks, tasks));
		    runtasks.start();
		   
		    boolean run = true;
		    while (run)
		    {
		    	System.out.println("listening"); // TODO for testing purposes
		    	
				Socket s = serverSocketForTask.accept();
				
				System.out.println("got socket"); // TODO for testing purposes
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				System.out.println("got output"); // TODO for testing purposes
				ObjectInputStream inputFromClient = new ObjectInputStream(s.getInputStream());
				System.out.println("got input"); // TODO for testing purposes
				
				
				System.out.println("read object"); // TODO for testing purposes
				Object temp = inputFromClient.readObject();
				if(temp instanceof Thread)
				{
					Socket tasksock = (Socket) inputFromClient.readObject();
					synchronized(tasks)
					{
						tasks.add((Thread) temp);
					}
					synchronized(socks)
					{
						socks.add(tasksock);
					}
				}
				else
				{
					System.out.println("get least"); // TODO for testing purposes
					synchronized(tasks)
					{
						out.writeObject(tasks.size());
					}
				}
		    }
		}
		catch (Exception e)
		{
		    // TODO: handle exception
			e.printStackTrace();
		}
    }
}
