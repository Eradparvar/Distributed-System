
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MasterServer
{
	private static ArrayList<Socket> slaveSockets = new ArrayList<>();
	
    public static void main(String[] args) throws IOException 
    {
    	
		// Hardcode in IP and Port here if required
		args = new String[] { "100" };
	
		if (args.length != 1)
		{
		    System.err.println("Usage: java MasterServer <port number>");
		    System.exit(1);
		}
		
		
		try(FileReader file = new FileReader("SlaveHostsAndPorts.txt");
				Scanner scan = new Scanner(file);)
		{
			String host;
			int port;
			while(scan.hasNext())
			{
				host = scan.nextLine();
				port = scan.nextInt();
				ServerSocket serverSocket = new ServerSocket(port);
				slaveSockets.add(serverSocket.accept());
				//slaveSockets.add(new Socket(host, port));
			}
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		
		/*
		boolean addslave = true;
		// try with resources
		try (Scanner scan = new Scanner(System.in);)
		{
			String host, temp;
			int port;
			while(addslave)
			{
				System.out.println("Enter a hostname, or enter 'exit' to exit:");
				temp = scan.nextLine();
				if(temp.equalsIgnoreCase("exit"))
					addslave = false;
				else
				{
					host = temp;
					System.out.println("Enter a port number:");
					port = scan.nextInt();
					slaveSockets.add(new Socket(host, port));
				}
			}
		} 
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		*/
		
	
		int portNumber = Integer.parseInt(args[0]);
		boolean listening = true;
		
		try(ServerSocket serverSocket = new ServerSocket(portNumber);)
		{
		    while (listening)
		    {
		    	System.out.println("listening"); // TODO for testing purposes
				// accepts clients request
				Socket clientSocket = serverSocket.accept();
				// creates thread to deal with client
				new Thread(new MasterServerThread(clientSocket, slaveSockets)).start();
		    }
		} 
		catch (Exception e)
		{
		    // TODO: handle exception
			e.printStackTrace();
		}
    }
}
