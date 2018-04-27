import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.lang.model.SourceVersion;

public class MasterServerThread implements Runnable {
    // Master
    Socket slave01 = null; // new Socket(host, port);
    Socket slave02 = null; // new Socket(host, port);
    Socket slave03 = null; // new Socket(host, port);

    private ServerSocket serverSocket;
    private int id;
    private int slaveToRouteConnection;
    private Task taskToRoute;

    // A reference to the server socket is passed in, all threads share it
    public MasterServerThread(ServerSocket serverSocket, int slaveToRouteConnection) {
	this.serverSocket = serverSocket;
	this.slaveToRouteConnection = slaveToRouteConnection;
    }

    @Override
    public void run() {
	Task clientsTask;
	try {
	    // Accepts task from client. Clients request is saved in clientsTask
	    Socket clientSocket = serverSocket.accept();
	    ObjectInputStream inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
	    clientsTask = (Task) inputFromClient.readObject();
	    boolean taskCompletionStatus = false;
	    Task finishedTask;
	    // pass the task to the server with the least connections
	    switch (slaveToRouteConnection) {
	    case 1:
		ObjectOutputStream outputToSlave01 = new ObjectOutputStream(slave01.getOutputStream());
		outputToSlave01.writeObject(taskToRoute);
		// Receives confirmation from slave that the task was successful
		ObjectInputStream inputFromSlave01 = new ObjectInputStream(slave01.getInputStream());
		finishedTask = (Task) inputFromSlave01.readObject();
		finishedTask.setWhichSlaveDidTask(1);
		System.out.println("Slave 1 - Task Status: " + taskCompletionStatus);
		break;

	    case 2:
		ObjectOutputStream outputToSlave02 = new ObjectOutputStream(slave02.getOutputStream());
		outputToSlave02.writeObject(taskToRoute);
		// Receives confirmation from slave that the task was successful
		ObjectInputStream inputFromSlave02 = new ObjectInputStream(slave02.getInputStream());
		finishedTask = (Task) inputFromSlave02.readObject();
		finishedTask.setWhichSlaveDidTask(2);
		System.out.println("Slave 2 - Task Status: " + taskCompletionStatus);
		break;

	    case 3:
		ObjectOutputStream outputToSlave03 = new ObjectOutputStream(slave03.getOutputStream());
		outputToSlave03.writeObject(taskToRoute);
		// Receives confirmation from slave that the task was successful
		ObjectInputStream inputFromSlave03 = new ObjectInputStream(slave03.getInputStream());
		finishedTask = (Task) inputFromSlave03.readObject();
		finishedTask.setWhichSlaveDidTask(3);
		System.out.println("Slave 3 - Task Status: " + taskCompletionStatus);
		break;

	    default:
		System.err.println("Error: Not valid -slaveToRouteConnection- value  ");
		break;
	    }
	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    // ArrayList<Thread> threads = new ArrayList<Thread>();
    // for (int ID = 0; ID < THREADS; ID++) {
    // Thread client = new Thread(new MasterServerThread(serverSocket, ID));
    // client.start();
    // threads.add(client);

}