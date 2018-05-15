
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.portable.InputStream;

public class ClientServer {
    public static void main(String[] args) throws IOException {
	
	// Hardcode in IP and Port here if required
	args = new String[] { "127.0.0.1", "7" };

	if (args.length != 2) {
	    System.err.println("Usage: java ClientServer <host name> <port number>");
	    System.exit(1);
	}

	String hostName = args[0];
	int portNumber = Integer.parseInt(args[1]);

	try {
	    System.out.println("Started clientServer");
	    Socket socket = new Socket(hostName, portNumber);
	    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());// send to server
	    Task clientTask = new Task("image", 2); // create new task to send
	    out.writeObject(clientTask); // sends Task to MasterServer
	    System.out.println("ClientServer sent task to master :). See Master if was succes");
	    System.out.println("ClientServer now waiting for response from Slave");
	    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());// Receives from server
	    Task taskResult = (Task) in.readObject();// Receives success and name of message from server
	    System.out.println(taskResult.toString());

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