import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

//The program can use this class to look up for which slaves there in the program.
public class SlaveList {
    final int NUM_SALVES = 3;
    static int roundRobbinCounter = 0;
    Socket[] slaveSockets = new Socket[NUM_SALVES];

    public SlaveList() {
	// try with resources
	try (Socket slaveSocket00 = new Socket("localhost", 102);
		Socket slaveSocket01 = new Socket("localhost", 102);
		Socket slaveSocket02 = new Socket("localhost", 102);) {
	    slaveSockets[0] = slaveSocket00;
	    slaveSockets[1] = slaveSocket01;
	    slaveSockets[2] = slaveSocket02;
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public Socket getTaskSocketSlaveNum(int slaveNumber) throws Exception {
	return slaveSockets[slaveNumber];
    }

    // Finds the slave with the least connections
    public int findLeastConnection() throws IOException {
	// ---*** Hirsch --This method needs attention ---****////
	/*
	 * sudu code 1) ask each slave how many connections they currently have. 2) Find
	 * the slave with the least connection. If tie then use roundRobbin scheme. You
	 * can use % for roundRobbon scheme. Also, roundRobbinCounter need to be thread
	 * safe 3)return the slave number. Example, if there are 3 slaves and slave # 2
	 * has the least connection return an integer 2.
	 * 
	 */

	// ObjectOutputStream outSlave01, outSlave02, outSlave03;
	// ObjectInputStream inSlave01, inSlave02, inSlave03;
	// outSlave01 = new
	// ObjectOutputStream(getSocketNumConnectionSlave01().getOutputStream());
	// outSlave02 = new
	// ObjectOutputStream(getSocketNumConnectionSlave02().getOutputStream());
	// outSlave03 = new
	// ObjectOutputStream(getSocketNumConnectionSlave03().getOutputStream());
	// // Receive from slave
	// inSlave01 = new
	// ObjectInputStream(getSocketNumConnectionSlave01().getInputStream());
	// inSlave02 = new
	// ObjectInputStream(getSocketNumConnectionSlave02().getInputStream());
	// inSlave03 = new
	// ObjectInputStream(getSocketNumConnectionSlave03().getInputStream());
	//
	// int slave01Connections = 0, slave02Connections, slave03Connections;
	int result = -1;
	// final int NUM_OF_SLAVES = 3;
	// int[] numConnection = new int[NUM_OF_SLAVES];
	// try {
	// outSlave01.writeObject("Slave01 many connections do you have?");
	// numConnection[0] = inSlave01.readInt();
	// outSlave02.writeObject("Slave02 many connections do you have?");
	// numConnection[1] = inSlave02.readInt();
	// outSlave03.writeObject("Slave03 many connections do you have?");
	// numConnection[2] = inSlave03.readInt();
	//
	// // finds the least connections of the 3 slaves
	// if (slave01Connections < slave02Connections && slave01Connections <
	// slave03Connections)
	// result = 1;
	// else if (slave02Connections < slave01Connections && slave02Connections <
	// slave03Connections)
	// result = 2;
	// else if (slave03Connections < slave01Connections && slave03Connections <
	// slave02Connections)
	// result = 3;
	// else
	// // tie, to break the tie we use roundRobbin
	// if (roundRobbinCounter == 1) {
	// result = 1;
	// changeRoundRobbinCounter();
	// } else if (roundRobbinCounter == 2) {
	// result = 2;
	// changeRoundRobbinCounter();
	// } else if (roundRobbinCounter == 3) {
	// result = 3;
	// changeRoundRobbinCounter();
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	return result;
	//
	// return slave01Connections;
	//
    }

    // RoundRobbinCounter variable is accessed by multiple threads at the same time.
    private synchronized static void changeRoundRobbinCounter() {
	roundRobbinCounter = roundRobbinCounter % 3;
    }

}
