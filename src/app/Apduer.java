package app;

import mvc.model.ApduData;
import mvc.view.ApduListFrame;

public class Apduer {

	//TODO: move into another package
	
	public static void main(String[] args) {
		
		ApduData commandData = new ApduData();
		ApduData responseData = new ApduData();	
		
		//start entrypoint
		//get settings
		//start relaysession
		//start apdulistframe
		
		ApduListFrame view = new ApduListFrame(responseData, commandData);
		view.setVisible(true);


//		RelayController controller = new RelayController(int listenPort, String remoteHost, int remotePort);
//		controller.addModel(commandData, responseData);
//		controller.startRelaySession();
		
		
	}
	
}
