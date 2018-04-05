import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DistributedMasterServer {

    public static void main(String[] args) {
	if (args.length != 1) {
	    System.err.println("Usage: java MasterServer <port number>");
	    System.exit(1);
	}

	int portNumber = Integer.parseInt(args[0]);
	boolean listening = true;
	int threadID = 100;

	try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
	    while (listening) {
		new multiServerThread(serverSocket.accept(), threadID++).start();
	    }
	} catch (IOException e) {
	    System.err.println("Could not listen on port " + portNumber);
	    System.exit(-1);
	}

    }
}
