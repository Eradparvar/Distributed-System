
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

	// Hardcode in Port here if required
	args = new String[] { "localhost", "102" };

	if (args.length != 2) {
	    System.err.println("Usage: java ClientServer <host name> <port number>");
	    System.exit(1);
	}

	ArrayList<Socket> socksDir = new ArrayList<>(); // list of sockets used to return the completed task to
							// MasterServerThread
	ArrayList<Thread> tasks = new ArrayList<>(); // list of tasks to run

	// this thread runs the tasks one by one
	Thread runtasks = new Thread(new SlaveServerProcessTasksThread(socksDir, tasks));
	runtasks.start();

	// this while loop keeps getting tasks and adding them to the list, or getting
	// requests for the number of tasks.
	boolean runSlaveServer = true;
	    while (runSlaveServer) {
	try // should this be outside the while loop?

	{
	    Socket slaveSocket = new Socket(args[0], Integer.parseInt(args[1]));
	    System.out.println("listening for master"); // TODO for testing purposes

	    ObjectOutputStream outputToMaster = new ObjectOutputStream(slaveSocket.getOutputStream()); // for sending
												       // the
	    // number of tasks (me- or
	    // completed tasks)
	    // to MasterServerThread
	    System.out.println("got outputStream"); // TODO for testing purpose
	    ObjectInputStream inputFromMaster = new ObjectInputStream(slaveSocket.getInputStream()); // for getting
												     // stuff from
	    // MasterServerThread
	    System.out.println("got inputStream"); // TODO for testing purposes
	    
		System.out.println("reading object"); // TODO for testing purposes
		Object temp = inputFromMaster.readObject(); // get a object from MasterServerThread
		if (temp instanceof Thread) // if the object is a task
		{
		    System.out.println("add thread"); // TODO for testing purposes

		    synchronized (tasks) {
			tasks.add((Thread) temp); // add the thread to the list of threads we need to run
			System.out.println("add sock"); // TODO for testing purposes
			socksDir.add(slaveSocket); // add the socket so we know where to send the completed task
		    }
		} else // if the object is not a task, that means MasterServerThread wants the number
		       // of tasks
		{
		    System.out.println("get least"); // TODO for testing purposes
		    synchronized (tasks) {
			outputToMaster.writeObject(tasks.size()); // send the number of tasks
		    }
		    System.out.println("Done get least");
		}
	    
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	    System.exit(1);
	}}

	try {
	    // the following for loop closes resource leaks
	    for (int i = 0; i < socksDir.size(); i++) {
		socksDir.get(i).close();
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
