import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.print.attribute.standard.Media;

public class MasterServer {

    public static void main(String[] args) throws IOException {

	// Hardcode in IP and Port here if required
	args = new String[] { "7" };

	if (args.length != 1) {
	    System.err.println("Usage: java MasterServer <port number>");
	    System.exit(1);
	}

	int portNumber = Integer.parseInt(args[0]);
	int slaveToRouteConnection;
	boolean listening = true;
	SlaveList slavesList = new SlaveList();// stores the slave socket class info
	try {
	    ServerSocket serverSocket = new ServerSocket(portNumber);
	    System.out.println("MasterServer created serverSocket on port " + portNumber);
	    while (listening) {
		System.out.println("MaterServer about to find slave with least connection");
		// Finds which slave to route connection to
		slaveToRouteConnection = slavesList.findLeastConnection();
		System.out.println("Found slave with least connection. Slave#" + slaveToRouteConnection);

		System.out.println("MasterServer now waiting for client reqest");
		// accepts clients request
		Socket clientSocket = serverSocket.accept();
		System.out.println("MasterServer recived client reqest :)");
		// creates thread to deal with client
		System.out.println("MasterServer about to create a thread to deal with client");
		new Thread(new MasterServerThread(clientSocket, slaveToRouteConnection, slavesList)).start();
		System.out.println(
			"MasterServer created MasterServerThread to deal with client: See log MasterServerThread for more details");

	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}

    }

}
