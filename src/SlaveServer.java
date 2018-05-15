import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.javatuples.Pair;

public class SlaveServer {
    static ArrayList<Thread> tasks = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
	
	try {

	    // Hardcode in Port here if required
	    args = new String[] { "102", "202" };
	    int portNumberToListenForTask = Integer.parseInt(args[0]);
	    int portNumberToListenForNumConnection = Integer.parseInt(args[1]);

	    if (args.length != 2) {
		System.err.println("Usage: java SlaveServer <port number>");
		System.exit(1);
	    }
	    System.out.println("SlaveServer Running");
	    ServerSocket serverSocketForTask = new ServerSocket(portNumberToListenForTask);
	    ServerSocket serverSocketForNumConnection = new ServerSocket(portNumberToListenForNumConnection);
	    System.out.println("SlaveServer: created 1)serverSocketForTask and 2)serverSocketForNumConnection");
	    LinkedList<Pair<Socket, Task>> tasksQueue = new LinkedList<>();
	    System.out.println("Started  SlaveServerHowManyConnectionsSocketThreads");
	    new Thread(new SlaveServerHowManyConnectionsSocketThreads(serverSocketForNumConnection, tasksQueue))
		    .start();
	    System.out.println("Started SlaveServerProcessTasksThread");
	    new Thread(new SlaveServerProcessTasksThread(tasksQueue)).start();
	    // Accepts connection and creates a thread to deal with task
	    boolean run = true;
	    while (run) {
		System.out.println("SalveServer entering while: wiating for client reqest");
		Socket clientsTaskSocket = serverSocketForTask.accept();
		System.out.println("SlaveServer got client Socket");
		new Thread(new SlaveServerAcceptsIncommingMessegesThread(clientsTaskSocket, tasksQueue)).start();
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}

    }

    

}
