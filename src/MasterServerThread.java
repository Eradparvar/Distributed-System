
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

//deals with clients request
//send the Task to slave
public class MasterServerThread implements Runnable
{
    private Socket clientSocket;
    private Thread task;
    private ArrayList<Socket> socks = new ArrayList<>();

    public MasterServerThread(Socket clientSocket, ArrayList<Socket> s)
    {
		this.clientSocket = clientSocket;
		this.socks = s;
    }

    @Override
    public void run()
    {
		try
		{
		    boolean run = true;
		    while (run)
		    {
				// from client
		    	ObjectOutputStream client = new ObjectOutputStream(clientSocket.getOutputStream());
		    	ObjectInputStream inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
				task = (Thread) inputFromClient.readObject();
				
				System.out.println("read task"); // TODO for testing purposes
				
				int a = findLeastConnection();
				ObjectOutputStream out = new ObjectOutputStream(socks.get(a).getOutputStream());
				System.out.println("read out"); // TODO for testing purposes
				ObjectInputStream in = new ObjectInputStream(socks.get(a).getInputStream());
				
				System.out.println("read in"); // TODO for testing purposes
				
				// send the task to the slave
				out.writeObject(task);
				System.out.println("wrote to slave"); // TODO for testing purposes
				
				// tell the slave who to send the completed task
				//out.writeObject(clientSocket);
				
				task = (Thread) in.readObject();
				System.out.println("got back task"); // TODO for testing purposes
				
				client.writeObject(task);
				
				System.out.println("running"); // TODO for testing purposes
		    }
		    
		} 
		catch (IOException | ClassNotFoundException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} 
		catch (Exception e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		finally
		{
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
    
    // Finds the slave with the least connections
    public int findLeastConnection() throws Exception
    {
    	int x = 0;
    	int min = Integer.MAX_VALUE;
    	int temp = -1;
    	for(int i=0; i<socks.size() && min!=0; i++)
    	{
    		ObjectOutputStream out = new ObjectOutputStream(socks.get(i).getOutputStream());
    		ObjectInputStream in = new ObjectInputStream(socks.get(i).getInputStream());
    		out.writeObject(0);
    		System.out.println("block"); // TODO for testing purposes
    		boolean a = true;
    		while(a)
    		{
    			System.out.print("."); // TODO for testing purposes
    			try
    			{
    				temp = (int) in.readObject();
    				a = false;
    			}
    			catch (EOFException e)
    			{
    				
    			}
    		}
    		System.out.println("block done"); // TODO for testing purposes
    		//temp = in.readInt();
    		if(temp == -1)
    			throw new Exception();
    		if(min > temp)
    		{
    			min = temp;
    			x = i;
    		}
    	}
    	System.out.println("flc done"); // TODO for testing purposes
    	return x;
    }
}