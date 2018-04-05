import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class multiServerThread extends Thread {

    private Socket socket = null;

    public multiServerThread(Socket socket, int id) {
	super("multiServerThread -- ID " + id);
	this.socket = socket;
    }

    public void run() {

	try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
	    String inputLine, outputLine;
	    ThreadProtocol kkp = new ThreadProtocol();
	    outputLine = kkp.processInput(null);
	    out.println(outputLine);

	    while ((inputLine = in.readLine()) != null) {
		outputLine = kkp.processInput(inputLine);
		out.println(outputLine);
		if (outputLine.equals("Bye"))
		    break;
	    }
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
