
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
    private Thread taskToRoute;
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
				ObjectInputStream inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
				taskToRoute = (Thread) inputFromClient.readObject();
				
				System.out.println("read task"); // TODO for testing purposes
				
				ObjectOutputStream out = new ObjectOutputStream(socks.get(findLeastConnection()).getOutputStream());
				
				System.out.println("read out"); // TODO for testing purposes
				
				// send the task to the slave
				out.writeObject(taskToRoute);
				
				// tell the slave who to send the completed task
				out.writeObject(clientSocket);
				
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
    }
    
    // Finds the slave with the least connections
    public int findLeastConnection() throws IOException
    {
    	int x = 0;
    	int min = Integer.MAX_VALUE;
    	int temp;
    	for(int i=0; i<socks.size() && min!=0; i++)
    	{
    		ObjectOutputStream out = new ObjectOutputStream(socks.get(i).getOutputStream());
    		ObjectInputStream in = new ObjectInputStream(socks.get(i).getInputStream());
    		out.writeObject(0);
    		System.out.println("block"); // TODO for testing purposes
    		while(in.available()==0);
    		System.out.println("block done"); // TODO for testing purposes
    		temp = in.readInt();
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