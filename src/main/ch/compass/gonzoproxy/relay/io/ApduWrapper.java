package ch.compass.gonzoproxy.relay.io;

import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public interface ApduWrapper {

	public byte[] wrap(Apdu apdu);

}
