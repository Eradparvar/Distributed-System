
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MasterServer {
    private static ArrayList<Socket> slaveSocketsList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

	// Hardcode in IP and Port here if required
	args = new String[] { "100" };

	if (args.length != 1) {
	    System.err.println("Usage: java MasterServer <port number>");
	    System.exit(1);
	}

	// this try/catch block creates sockets to communicate with each slave, reading
	// the port
	// numbers (necessary for the creation of the socket) from a file named
	// SlaveHostsAndPorts.txt
	try (FileReader file = new FileReader("SlaveHostsAndPorts.txt"); Scanner scan = new Scanner(file);) {
	    int slavePortNum;
	    while (scan.hasNext()) {
		slavePortNum = scan.nextInt();
		ServerSocket serverSocket = new ServerSocket(slavePortNum); // TODO close resource leak
		slaveSocketsList.add(serverSocket.accept());
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// keeping the following commented code around in case we ever need it.
	// it makes the user enter the slaves's port numbers manually, instead of using
	// a file
	/*
	 * boolean addslave = true; // try with resources try (Scanner scan = new
	 * Scanner(System.in);) { String host, temp; int port; while(addslave) {
	 * System.out.println("Enter a hostname, or enter 'exit' to exit:"); temp =
	 * scan.nextLine(); if(temp.equalsIgnoreCase("exit")) addslave = false; else {
	 * host = temp; System.out.println("Enter a port number:"); port =
	 * scan.nextInt(); slaveSockets.add(new Socket(host, port)); } } } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 */

	// the following while loop spawns a new MasterServerThread for each client.
	boolean listening = true;
	try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));) {
	    while (listening) {
		System.out.println("listening for client"); // TODO for testing purposes

		// the following line waits for a new client to connect
		Socket clientSocket = serverSocket.accept();

		// creates thread to deal with client
		// this thread is necessary so that we can connect to clients while, say,
		// sending tasks to slaves
		new Thread(new MasterServerThread(clientSocket, slaveSocketsList)).start();
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}
    }
}
