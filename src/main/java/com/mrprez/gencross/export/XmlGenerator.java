package com.mrprez.gencross.export;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.util.XmlExport;

public class XmlGenerator extends FileGenerator {

	@Override
	public void write(Personnage personnage, OutputStream os)throws FileNotFoundException, IOException {
		XmlExport xmlCreator = new XmlExport();
		Document document = xmlCreator.buildXmlToExport(personnage);
		OutputFormat format = new OutputFormat("\t", true, "UTF-8");
		XMLWriter writer = new XMLWriter(os, format);
		writer.write(document);
	}

	@Override
	public String getOutputExtension() {
		return "xml";
	}

}
