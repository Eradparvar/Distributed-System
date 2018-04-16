import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class Slave {
    public static void main(String[] args) throws IOException {

	// Hardcode in Port here if required
	// args = new String[] {"30121"};

	if (args.length != 1) {
	    System.err.println("Usage: java Slave <port number>");
	    System.exit(1);
	}

	int portNumber = Integer.parseInt(args[0]);
	int queLength = 0;

	// To do-------------
	// listens for master - retuns to master currnt connection

	try {// to connect to master
	    ServerSocket serverSocket = new ServerSocket(portNumber);
	    Socket socket = serverSocket.accept();
	    ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());// Receives from MasteServer
	    Task task = (Task) inFromServer.readObject();
	    boolean taskResult = task.start();
	    // Task done. Now sending to master server
	    ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());// send to masterServer
	    outToServer.writeBoolean(taskResult);

	} catch (IOException | ClassNotFoundException e) {
	    // System.err.println("Could not listen on port " + portNumber);
	    System.exit(-1);
	}

    }

}
