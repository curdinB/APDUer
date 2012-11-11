package relay.session;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import mvc.model.ApduData;

/**
 * 
 * @author Curdin Barandun
 * 
 * A relay session consists of two forwarder threads, one for each side of the relay
 *
 */
public class RelaySession {
	
	private final int PORT = 1234;
	private ServerSocket serverSocket;
	private Socket initiatorSocket;
	private ApduData commandData;
	private ApduData responseData;
	
	// TODO: initialize model somewhere else :>
	
	
	
	public RelaySession(Socket target, ApduData commandData, ApduData responseData) {
		this.commandData = commandData;
		this.responseData = responseData;
		establishConnection();
		initForwarding(target);
	}

	private void establishConnection() {
		try {
			serverSocket = new ServerSocket(PORT);
			initiatorSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initForwarding(Socket target) {
		new Thread(new Forwarder(initiatorSocket,target, commandData)).start();
		new Thread(new Forwarder(target, initiatorSocket, responseData)).start();
	}

}