
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
	    args = new String[] { "localhost", "102"};

	    if (args.length != 2)
	    {
	    	System.err.println("Usage: java ClientServer <host name> <port number>");
			System.exit(1);
	    }
	    
		
	    ArrayList<Socket> socks = new ArrayList<>();
	    ArrayList<Thread> tasks = new ArrayList<>();
	    
	    
	    Thread runtasks = new Thread(new SlaveServerProcessTasksThread(socks, tasks));
	    runtasks.start();
	   
	    boolean run = true;
	    while (run)
	    {
	    	try//(//ServerSocket serverSocketForTask = new ServerSocket(Integer.parseInt(args[0]));)
			{
	    		Socket s = new Socket(args[0], Integer.parseInt(args[1]));
	    		
		    	System.out.println("listening"); // TODO for testing purposes
		    	
				//Socket s = serverSocketForTask.accept();
				
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
				System.out.println("got output"); // TODO for testing purposes
				ObjectInputStream inputFromClient = new ObjectInputStream(s.getInputStream());
				System.out.println("got input"); // TODO for testing purposes
				
				
				System.out.println("read object"); // TODO for testing purposes
				Object temp = inputFromClient.readObject();
				if(temp instanceof Thread)
				{
					System.out.println("add thread"); // TODO for testing purposes
					//Socket tasksock = (Socket) inputFromClient.readObject();
					synchronized(tasks)
					{
						tasks.add((Thread) temp);
					}
					System.out.println("add sock"); // TODO for testing purposes
					synchronized(socks)
					{
						socks.add(s);
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
	    	catch (Exception e)
			{
			    // TODO: handle exception
				e.printStackTrace();
				System.exit(1);
			}
		}
	    
		try
		{
			for(int i=0; i<socks.size(); i++)
			{
				socks.get(i).close();
			}
		}
		catch (Exception e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
    }
}
