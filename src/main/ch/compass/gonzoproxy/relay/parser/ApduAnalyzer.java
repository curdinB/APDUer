package ch.compass.gonzoproxy.relay.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;

import ch.compass.gonzoproxy.mvc.model.Apdu;

public class ApduAnalyzer {

	private static final String TEMPLATE_FOLDER = "templates/";

	private File[] templateFiles;
	private ArrayList<ApduTemplate> templates = new ArrayList<ApduTemplate>();

	private AsciiApduParser asciiParser = new AsciiApduParser();
	private Parser selectedParser;

	public ApduAnalyzer() {
		locateTemplateFiles();
		loadTemplates();
	}

	private void locateTemplateFiles() {
		File folder = new File(TEMPLATE_FOLDER);
		templateFiles = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".apdu");
			}
		});
	}

	private void loadTemplates() {
		for (int i = 0; i < templateFiles.length; i++) {
			try(InputStream fileInput = new FileInputStream(templateFiles[i])) {
				Yaml beanLoader = new Yaml();
				ApduTemplate template = beanLoader.loadAs(fileInput, ApduTemplate.class);
				templates.add(template);
				System.out.println("template " + template.getApduDescription() + " added");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void processApdu(Apdu apdu) {
		findMatchingParser(apdu);
		if (!parseByTemplate(apdu))
			parseByDefault(apdu);
	}

	private void parseByDefault(Apdu apdu) {
		//TODO: implement
	}

	private boolean parseByTemplate(Apdu apdu) {
		for (ApduTemplate template : templates) {
			if (selectedParser.templateIsAccepted(template)) {
				return selectedParser.tryParse(template);
			}
		}
		return false;
	}

	// TODO: fix
	private void findMatchingParser(Apdu apdu) {
		asciiParser.setProcessingApdu(apdu);
		selectedParser = asciiParser;
	}
	
	
	
	

}