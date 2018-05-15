import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.javatuples.Pair;

public class SlaveServerHowManyConnectionsSocketThreads implements Runnable {
    private final static Logger LOGGER = Logger.getLogger("SlaveServer_Log");

    ServerSocket serverSocket;
    LinkedList<Pair<Socket, Task>> taskQueue;

    public SlaveServerHowManyConnectionsSocketThreads(ServerSocket serverSocketForNumConnection2,
	    LinkedList<Pair<Socket, Task>> taskQueue) {
	this.serverSocket = serverSocketForNumConnection2;
	this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
	try {
	    setupLogger();
	    boolean listening = true;
	    while (listening) {
		LOGGER.finest("SlaveServer Running");
		// accept connection with master
		Socket socket = serverSocket.accept();
		// Receives from masterServer
		ObjectInputStream inFromMaster = new ObjectInputStream(socket.getInputStream());
		// Master asks how many connections do you have --dummy value
		Object masterReqest = (Object) inFromMaster.readObject();
		// send to masterServer
		ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
		// Slave Responds with current Tasks on taskQueue
		outToServer.writeInt(taskQueue.size());
	    }
	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private static void setupLogger() {
	LOGGER.setLevel(Level.ALL);
	try {
	    FileHandler fhandler = new FileHandler("SlaveServerLogfile.txt",true);
	    SimpleFormatter sformatter = new SimpleFormatter();
	    fhandler.setFormatter(sformatter);
	    LOGGER.addHandler(fhandler);

	} catch (IOException ex) {
	    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
	} catch (SecurityException ex) {
	    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
	}
    }
}
