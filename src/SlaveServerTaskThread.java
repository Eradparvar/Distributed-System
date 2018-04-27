import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SlaveServerTaskThread implements Runnable {

    @Override
    public void run() {
	boolean running = true;
	while (running) {

	    try {// to connect to master for task
		    ServerSocket serverSocket = new ServerSocket(portNumber);
		    Socket socket = serverSocket.accept();
		    ObjectInputStream inFromMaster = new ObjectInputStream(socket.getInputStream());// Receives from MasteServer
		    Task task = (Task) inFromMaster.readObject();
		    boolean taskResult = task.startTask();
		    // Task done. Now sending to master server
		    ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());// send to masterServer
		    outToServer.writeBoolean(taskResult);

		} catch (IOException | ClassNotFoundException e) {
		    // System.err.println("Could not listen on port " + portNumber);
		    System.exit(-1);
		}
	}

    }
}
