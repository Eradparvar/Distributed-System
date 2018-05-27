
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

// sends the Tasks from a certain client to the slave with least connections and returns the completed task to the client
public class MasterServerThread implements Runnable {
    private Socket clientSocket;
    private Thread task;
    private ArrayList<Socket> slaveSocketsList = new ArrayList<>();

    public MasterServerThread(Socket clientSocket, ArrayList<Socket> slaveSocketsList) {
	this.clientSocket = clientSocket;
	this.slaveSocketsList = slaveSocketsList;
    }

    @Override
    public void run() {
	try {
	    ObjectOutputStream outputToClient = new ObjectOutputStream(clientSocket.getOutputStream()); // used to send things
												// to client
	    ObjectInputStream inputFromClient = new ObjectInputStream(clientSocket.getInputStream()); // used to get
												      // things from
												      // client

	    boolean run = true;
	    while (run) {
		// the following line waits until the client sends a task
		task = (Thread) inputFromClient.readObject();
		
		System.out.println("read task"); // TODO for testing purposes

		int slaveNumWithLeastCon = findLeastConnection(); // 'a' will be the task with fewest tasks
		ObjectOutputStream out = new ObjectOutputStream(slaveSocketsList.get(slaveNumWithLeastCon).getOutputStream()); // used to send things
											 // to slave #'a'
		System.out.println("read out"); // TODO for testing purposes
		ObjectInputStream in = new ObjectInputStream(slaveSocketsList.get(slaveNumWithLeastCon).getInputStream()); // used to get things from
											     // slave #'a'

		System.out.println("read in"); // TODO for testing purposes

		out.writeObject(task); // send the task to the slave
		System.out.println("wrote to slave"); // TODO for testing purposes

		// TODO the following needs to be in a separate thread. It will block waiting
		// for the slave to send it the completed task
		task = (Thread) in.readObject(); // gets the completed task from the slave
		System.out.println("got back task"); // TODO for testing purposes
		outputToClient.writeObject(task); // sends the completed task back to the client

		System.out.println("running"); // TODO for testing purposes
	    }

	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    // the following try/catch block closes resource leaks
	    try {
		for (int i = 0; i < slaveSocketsList.size(); i++) {
		    slaveSocketsList.get(i).close();
		}
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    // Finds the slave with the least connections
    public int findLeastConnection() throws Exception {
	int x = 0; // the number of the slave with fewest tasks
	int min = Integer.MAX_VALUE; // the fewest number of tasks
	int temp = -1;
	for (int i = 0; i < slaveSocketsList.size() && min != 0; i++) // for each slave
	{
	    ObjectOutputStream out = new ObjectOutputStream(slaveSocketsList.get(i).getOutputStream()); // used to send to slave
	    ObjectInputStream in = new ObjectInputStream(slaveSocketsList.get(i).getInputStream()); // used to get from slave
	    out.writeObject(0); // sending an int tells the slave to tell us how many tasks it has

	    System.out.println("block"); // TODO for testing purposes
	    // the following while loop does nothing until the slave tells us how many tasks
	    // it has
	    boolean a = true;
	    while (a) {
		System.out.print("."); // TODO for testing purposes
		try {
		    temp = (int) in.readObject();
		    a = false;
		} catch (EOFException e) {
		    // intentionally empty. although, maybe this should sleep for a short time.
		}
	    }
	    System.out.println("block done"); // TODO for testing purposes

	    // if temp is -1, something has gone wrong
	    if (temp == -1)
		throw new Exception();

	    if (min > temp) // if this slave has fewer task than any previous slave
	    {
		min = temp; // our number of tasks is now the fewest
		x = i; // our slave is now the one with the fewest tasks
	    }
	}
	System.out.println("flc done"); // TODO for testing purposes
	return x;
    }
}