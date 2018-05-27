
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServer {
    public static void main(String[] args) throws IOException {
	Thread taskResult;

	// Hardcode in IP and Port here if required
	args = new String[] { "localhost", "100" };

	if (args.length != 2) {
	    System.err.println("Usage: java ClientServer <host name> <port number>");
	    System.exit(1);
	}

	// TODO the following try/catch block probably needs to be spawned as a separate
	// thread in order to accommodate multiple tasks.
	try (Socket clientSocket = new Socket(args[0], Integer.parseInt(args[1]));) {
	    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());// used to send things to master

	    // create a new task to send.
	    // this is just for testing purposes, the final version has to create many
	    // tasks.
	    Thread clientTask = new Task("image", 0);

	    System.out.println("Client sending task to master"); // TODO for testing purposes
	    out.writeObject(clientTask); // sends Task to MasterServer

	    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());// used to get things from master

	    taskResult = (Thread) in.readObject();// gets the completed task from master

	    System.out.println(taskResult); // print the task
	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host " + args[0]);
	    e.printStackTrace();
	    System.exit(1);
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for the connection to " + args[0]);
	    e.printStackTrace();
	    System.exit(1);
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}