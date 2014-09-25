package com.mrprez.gencross.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.mrprez.gencross.Personnage;

public class EncodedXmlGenerator extends TemplatedFileGenerator {
	protected File template;
	
	@Override
	public void setTemplate(File template) throws FileNotFoundException, IOException {
		this.template = template;
	}

	@Override
	public void write(Personnage personnage, OutputStream os) throws FileNotFoundException, IOException {
		FileReader reader = new FileReader(template);
		OutputStreamWriter writer = new OutputStreamWriter(os);
		try{
			replace(reader, writer, personnage);
		}finally{
			reader.close();
		}

	}

	@Override
	public String getOutputExtension() {
		return "xml";
	}

	@Override
	public String getTemplateFileExtension() {
		return "xml";
	}

	@Override
	public String getTemlpateFileExtensionDescription() {
		return "Extensible Markup Language";
	}
	
	

}
