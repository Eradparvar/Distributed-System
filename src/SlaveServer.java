import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SlaveServer {
    public static void main(String[] args) throws IOException {
	int numOfConncetions = 0;
	// Hardcode in Port here if required
	// args = new String[] {"30121"};

	if (args.length != 1) {
	    System.err.println("Usage: java Slave <port number>");
	    System.exit(1);
	}

	int portNumber = Integer.parseInt(args[0]);

	// portNumber need to enable way to set the values for here and next portnumber
	// Connects to master to send how many connections it currently has
	try {// to connect to master for current connections
	    ServerSocket serverSocket = new ServerSocket(portNumber);
	    Socket socket = serverSocket.accept();
	    ObjectInputStream inFromMaster = new ObjectInputStream(socket.getInputStream());// Receives from MasteServer
	    Object masterReqest = (Object) inFromMaster.readObject();// Master asks how many connections do you have
	    // Slave ans master how many onncetions it has
	    ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());// send to masterServer
	    outToServer.writeInt(numOfConncetions);

	} catch (IOException | ClassNotFoundException e) {
	    // System.err.println("Could not listen on port " + portNumber);
	    System.exit(-1);
	}

	int queLength = 0;
	// To do keep track of number of connections and possibly create a thread for
	// it****
	ArrayList<Thread> threads = new ArrayList<>();
	
	
    }

}
