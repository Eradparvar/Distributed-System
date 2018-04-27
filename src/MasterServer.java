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
	// will have to create a method where it all gets set up by itself ****
	// need to hardcode host and port numbers *****----*****
	Socket socketSlave01 = null; // = new Socket(host, port);
	Socket socketSlave02 = null; // = new Socket(host, port);
	Socket socketSlave03 = null;// = new Socket(host, port);

	if (args.length != 1) {
	    System.err.println("Usage: java MasterServer <port number>");
	    System.exit(1);
	}

	int portNumber = Integer.parseInt(args[0]);
	int slaveToRouteConnection;
	boolean listening = true;
	while (listening) {
	    ServerSocket serverSocket = new ServerSocket(portNumber);
	    // Finds which slave to route connection to
	    slaveToRouteConnection = findLeastConnection(socketSlave01, socketSlave02, socketSlave03);
	    // creates thread to deal with client
	    new MasterServerThread(serverSocket, slaveToRouteConnection);

	}

    }

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
	// Master --> slave

	ObjectOutputStream outSlave01 = new ObjectOutputStream(slave01.getOutputStream());
	ObjectOutputStream outSlave02 = new ObjectOutputStream(slave02.getOutputStream());
	ObjectOutputStream outSlave03 = new ObjectOutputStream(slave03.getOutputStream());
	// Receive from slave
	ObjectInputStream inSlave01 = new ObjectInputStream(slave01.getInputStream());
	ObjectInputStream inSlave02 = new ObjectInputStream(slave02.getInputStream());
	ObjectInputStream inSlave03 = new ObjectInputStream(slave03.getInputStream());

	int slave01Connections, slave02Connections, slave03Connections;
	int result = -1;

	try {
	    outSlave01.writeObject("Slave01 many connections do you have?");
	    slave01Connections = inSlave01.readInt();
	    outSlave02.writeObject("Slave02 many connections do you have?");
	    slave02Connections = inSlave02.readInt();
	    outSlave03.writeObject("Slave03 many connections do you have?");
	    slave03Connections = inSlave03.readInt();

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
		incrementRoundRobbinCounter();
	    } else if (roundRobbinCounter == 2) {
		result = 2;
		incrementRoundRobbinCounter();
	    } else if (roundRobbinCounter == 3) {
		result = 3;
		setRoundRobbinCounterToOne();
	    }
	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return result;

    }

    // RoundRobbinCounter is accessed by multiple threads at the same time.
    // synchronized makes the object threads safe.
    private synchronized static void incrementRoundRobbinCounter() {
	roundRobbinCounter++;

    }

    private synchronized static void setRoundRobbinCounterToOne() {
	roundRobbinCounter = 1;

    }
}
