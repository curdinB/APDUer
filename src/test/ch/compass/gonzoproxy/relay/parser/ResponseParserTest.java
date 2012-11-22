package ch.compass.gonzoproxy.relay.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.Field;

public class ResponseParserTest {

	@Test
	public void testSingleIdentifierTemplateAccepted() {
		ResponseParser parser = new ResponseParser();
		
		String processingApduFake = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Apdu apdu = new Apdu(libnfcInput.getBytes());
		apdu.setPlainApdu(processingApduFake.getBytes());
		parser.setProcessingApdu(apdu);
		
		ApduTemplate templateFake = new ApduTemplate();
		templateFake.getFields().add(new Field("testFieldName", "00", "testDescription"));
		
		assertTrue(parser.templateIsAccepted(templateFake));
	}
	
	@Test
	public void testSingleContentIdentifierAccepted(){
		
		ResponseParser parser = new ResponseParser();
		
		String processingApduFake = "00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		String libnfcInput = "C-APDU 000d: 00 a4 04 00 07 d2 76 00 00 85 01 01 00";
		Apdu apdu = new Apdu(libnfcInput.getBytes());
		apdu.setPlainApdu(processingApduFake.getBytes());
		parser.setProcessingApdu(apdu);
		
		ApduTemplate templateFake = new ApduTemplate();
		templateFake.getFields().add(new Field("idField2", "00", "testDescription"));
		templateFake.getFields().add(new Field("idField 2", "a4", "idfield 2"));
		templateFake.getFields().add(new Field("Lc", "04", "Content Length"));
		templateFake.getFields().add(new Field("Ci", "00", "Content Identifier"));
		templateFake.getFields().add(new Field("Content identifier", "07 d2 76", "Content Length"));
		
		assertTrue(parser.templateIsAccepted(templateFake));
	}

}
