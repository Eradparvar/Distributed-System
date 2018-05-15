import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//deals with clients request
//send the Task to slave
public class MasterServerThread implements Runnable {
    private final static Logger LOGGER = Logger.getLogger("SlaveServer_Log");

    private Socket clientSocket;
    private int slaveToRouteConnection;
    private Task taskToRoute;
    private SlaveList slaveList;

    public MasterServerThread(Socket clientSocket, int slaveToRouteConnection, SlaveList slavesList) {
	this.clientSocket = clientSocket;
	this.slaveToRouteConnection = slaveToRouteConnection;
	this.slaveList = slavesList;
    }

    @Override
    public void run() {
	setupLogger();
	try {
	    boolean run = true;
	    while (run) {
		// from client
		ObjectInputStream inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		taskToRoute = (Task) inputFromClient.readObject();
		LOGGER.finest("MasterServer -- MasterServerThread: recived task from client");

		// send taskToRoute to slave
		ObjectOutputStream outputToSlave = new ObjectOutputStream(
			slaveList.getTaskSocketSlaveNum(slaveToRouteConnection).getOutputStream());
		taskToRoute.setWhichSlaveHasTask(slaveToRouteConnection);
		LOGGER.finest("MasterServerThread: about to send task to slave");
		outputToSlave.writeObject(clientSocket);
		LOGGER.finest("MasterServerThread: sent task to slave :)");

	    }

	} catch (IOException | ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    private static void setupLogger() {
   	LOGGER.setLevel(Level.ALL);
   	try {
   	    FileHandler fhandler = new FileHandler("SlaveServerLogfile.txt");
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