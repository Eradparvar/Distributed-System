import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.javatuples.Pair;

//Process tasks on tasksQueue
public class SlaveServerProcessTasksThread implements Runnable {

    private LinkedList<Pair<Socket, Task>> tasksQueue;

    public SlaveServerProcessTasksThread(LinkedList<Pair<Socket, Task>> tasksQueue) {
	this.tasksQueue = tasksQueue;
    }

    @Override
    public void run() {
	boolean running = true;
	while (running) {
	    while (!tasksQueue.isEmpty()) {
		try {
		    Pair<Socket, Task> clientJob = tasksQueue.remove();
		    Task clientsTaskReqest = (Task) clientJob.getValue1();
		    clientsTaskReqest.startTask();
		    // send Task completion status to client
		    Socket clientsocket = (Socket) clientJob.getValue0();
		    ObjectOutputStream outputToClient = new ObjectOutputStream(clientsocket.getOutputStream());
		    outputToClient.writeObject(clientJob);
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	}

    }
}
