import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MasterServer {
    static int roundRobbinCounter = 1;

    public static void main(String[] args) throws IOException {
	// MASTER ---> SLAVE
	// need to hardcode host and port numbers *****----*****
	Socket socketSlave01 = null; // = new Socket(host, port);
	Socket socketSlave02 = null; // = new Socket(host, port);
	Socket socketSlave03 = null;// = new Socket(host, port);
	boolean listening = true;
	if (args.length != 1) {
	    System.err.println("Usage: java MasterServer <port number>");
	    System.exit(1);
	}

	int portNumber = Integer.parseInt(args[0]);
	int slaveToRouteConnection;

	while (listening) {
	    ServerSocket serverSocket = new ServerSocket(portNumber);
	    // Finds which slave to route connection to
	    slaveToRouteConnection = findLeastConnection(socketSlave01, socketSlave02, socketSlave03);
	    // creates thread to deal with client
	    new MasterServerThread(serverSocket, slaveToRouteConnection);

	}

    }

    // ArrayList<Thread> threads = new ArrayList<Thread>();
    // for (int ID = 0; ID < THREADS; ID++) {
    // Thread client = new Thread(new MasterServerThread(serverSocket, ID));
    // client.start();
    // threads.add(client);

    // to to---**
    // find slave with least connection
    // create a thread to pass the task to the slave to do
    // thread recives task
    // pass task to the server with the least connections
    // slave class ---**
    // accsept task
    // acopmpish task
    // send it back to client

    // find the slave with the least connections
    private static int findLeastConnection(Socket slave01, Socket slave02, Socket slave03) throws IOException {
	ObjectOutputStream outSlave01, outSlave02, outSlave03;
	ObjectInputStream inSlave01, inSlave02, inSlave03;

	outSlave01 = new ObjectOutputStream(slave01.getOutputStream());
	outSlave02 = new ObjectOutputStream(slave02.getOutputStream());
	outSlave03 = new ObjectOutputStream(slave03.getOutputStream());
	// Receive from slave
	inSlave01 = new ObjectInputStream(slave01.getInputStream());
	inSlave02 = new ObjectInputStream(slave02.getInputStream());
	inSlave03 = new ObjectInputStream(slave03.getInputStream());

	int slave01Connections, slave02Connections, slave03Connections;
	int result = -1;

	try {
	    outSlave01.writeObject("Slave01 many connections do you have?");
	    slave01Connections = (int) inSlave01.readObject();
	    outSlave02.writeObject("Slave02 many connections do you have?");
	    slave02Connections = (int) inSlave02.readObject();
	    outSlave03.writeObject("Slave03 many connections do you have?");
	    slave03Connections = (int) inSlave03.readObject();

	    // finds the least connections of the 3 slaves
	    if (slave01Connections < slave02Connections && slave01Connections < slave03Connections)
		result = 1;
	    else if (slave02Connections < slave01Connections && slave02Connections < slave03Connections)
		result = 2;
	    else if (slave03Connections < slave01Connections && slave03Connections < slave02Connections)
		result = 3;
	    else
	    // tie, to break the tie we use roundRobbin
	    if (roundRobbinCounter == 1) {
		result = 1;
		roundRobbinCounter++;
	    } else if (roundRobbinCounter == 2) {
		result = 2;
		roundRobbinCounter++;
	    } else if (roundRobbinCounter == 3) {
		result = 3;
		roundRobbinCounter = 1;
	    }
	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return result;

    }
}
