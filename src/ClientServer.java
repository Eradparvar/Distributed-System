
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServer
{
    public static void main(String[] args) throws IOException
    {
    	Thread taskResult;
    	
		// Hardcode in IP and Port here if required
		args = new String[] { "localhost", "100" };
		
		if (args.length != 2)
		{
		    System.err.println("Usage: java ClientServer <host name> <port number>");
		    System.exit(1);
		}
		
		
		try(Socket socket = new Socket(args[0], Integer.parseInt(args[1]));)
		{
		    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());// send to server
		    
		    Thread clientTask = new Task("image", 0); // create new task to send
		    
		    System.out.println("sending task"); // TODO for testing purposes
		    out.writeObject(clientTask); // sends Task to MasterServer
		    
		    
		    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());// Receives from server
		    taskResult = (Thread) in.readObject();// Receives success and name of message from server
		    
			System.out.println(taskResult);
		} 
		catch (UnknownHostException e)
		{
		    System.err.println("Don't know about host " + args[0]);
		    e.printStackTrace();
		    System.exit(1);
		} 
		catch (IOException e)
		{
		    System.err.println("Couldn't get I/O for the connection to " + args[0]);
		    e.printStackTrace();
		    System.exit(1);
		} 
		catch (ClassNotFoundException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
    }
}