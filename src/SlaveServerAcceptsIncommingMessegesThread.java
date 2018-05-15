import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.javatuples.Pair;

//Class accepts task and adds it to taskQueue
public class SlaveServerAcceptsIncommingMessegesThread implements Runnable {
   
    private Socket socketFromMaster;
    private LinkedList<Pair<Socket, Task>> taskQueue;

    public SlaveServerAcceptsIncommingMessegesThread(Socket socketFromMaster,
	    LinkedList<Pair<Socket, Task>> tasksQueue) {
	this.socketFromMaster = socketFromMaster;
	this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
	try {
	    // Receives clients socket from MasterServer
	    ObjectInputStream inFromMaster = new ObjectInputStream(socketFromMaster.getInputStream());
	    Socket clientsSocket = (Socket) inFromMaster.readObject(); // clients socket
	    // accepts the clients request and saves the task to taskQueue
	    ObjectInputStream inputFromClient = new ObjectInputStream(clientsSocket.getInputStream());
	    Task clientsTask = (Task) inputFromClient.readObject();
	    Pair<Socket, Task> jobInfo = new Pair<Socket, Task>(clientsSocket, clientsTask);
	    synchronized (taskQueue) {
		taskQueue.add(jobInfo);
	    }

	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    

}
