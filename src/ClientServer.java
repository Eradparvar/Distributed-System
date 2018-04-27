
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.omg.CORBA.portable.InputStream;

public class ClientServer {
    public static void main(String[] args) throws IOException {

	// Hardcode in IP and Port here if required
	// args = new String[] {"127.0.0.1", "30121"};

	if (args.length != 2) {
	    System.err.println("Usage: java EchoClient <host name> <port number>");
	    System.exit(1);
	}

	String hostName = args[0];
	int portNumber = Integer.parseInt(args[1]);

	try {
	    Socket socket = new Socket(hostName, portNumber);
	    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());// send to server
	    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());// Receives from server

	    Task clientTask = new Task("image", 2); // create new task to send
	    out.writeObject(clientTask); // sends Task to MasterServer

	    Task taskResult = (Task) in.readObject();// Receives success and name of message from server
	    System.out.println(taskResult);

	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host " + hostName);
	    System.exit(1);
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for the connection to " + hostName);
	    System.exit(1);
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}