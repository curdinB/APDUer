package ch.compass.gonzoproxy.relay.io;

import java.util.ArrayList;
import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.utils.ByteArraysUtils;

public class EtNfcApduExtractor implements ApduExtractor {

	private static final char EOC = '\n';

	private static final char DELIMITER = '#';


	public byte[] extractPacketsToQueue(byte[] buffer, Queue<Packet> apduQueue,
			int readBytes) {
		ArrayList<Integer> indices = ByteArraysUtils.getDelimiterIndices(buffer,
				DELIMITER);

		int startIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < indices.size() - 1; i++) {
			startIndex = indices.get(i);
			endIndex = indices.get(i + 1);
			int size = endIndex - startIndex;
			byte[] rawApdu = ByteArraysUtils.trim(buffer, startIndex, size);
			Packet apdu = splitApdu(rawApdu);
			apduQueue.add(apdu);
		}

		byte[] singleApdu = ByteArraysUtils.trim(buffer, endIndex, readBytes - endIndex);

		if (apduIsComplete(singleApdu)) {
			Packet apdu = splitApdu(singleApdu);
			apduQueue.add(apdu);
			return new byte[0];
		} else {
			return singleApdu;
		}
	}

	private boolean apduIsComplete(byte[] singleApdu) {
		return singleApdu[singleApdu.length - 1] == EOC;
	}

	private Packet splitApdu(byte[] rawApdu) {
		int size = getApduSize(rawApdu);
		byte[] preamble = getApduPreamble(rawApdu, size);
		byte[] plainApdu = getPlainApdu(rawApdu, size);
		byte[] trailer = getApduTrailer(rawApdu, size);
		Packet newApdu = new Packet(rawApdu);
		newApdu.setPreamble(preamble);
		newApdu.setOriginalPacketData(plainApdu);
		newApdu.setTrailer(trailer);
		newApdu.setSize(size);
		return newApdu;
	}

	private byte[] getApduTrailer(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if (rawApdu[i] == ':') {
				int endOfPlainApdu = i + 3 * size + 1;
				return ByteArraysUtils.trim(rawApdu, endOfPlainApdu, rawApdu.length
						- endOfPlainApdu);
			}
		}
		return null;
	}

	private byte[] getPlainApdu(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if (rawApdu[i] == ':') {
				return ByteArraysUtils.trim(rawApdu, i + 2, size * 3 - 1);
			}
		}
		return rawApdu;
	}

	private byte[] getApduPreamble(byte[] rawApdu, int size) {
		for (int i = 0; i < rawApdu.length; i++) {
			if (rawApdu[i] == ':') {
				return ByteArraysUtils.trim(rawApdu, 0, i + 2);
			}
		}
		return rawApdu;
	}

	private int getApduSize(byte[] rawApdu) {
		int value = 0;
		byte[] size = new byte[4];
		for (int i = 0; i < rawApdu.length; i++) {
			if (rawApdu[i] == ' ') {
				size[0] = rawApdu[i + 1];
				size[1] = rawApdu[i + 2];
				size[2] = rawApdu[i + 3];
				size[3] = rawApdu[i + 4];
				value = Integer.parseInt(new String(size), 16);
				System.out.println("Size: " + value);
				return value;
			}
		}
		return value;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "etnfc";
	}
}
