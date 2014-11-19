package com.mrprez.gencross.export;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mrprez.gencross.Personnage;

public abstract class FileGenerator {
	private static Map<String, Class<? extends FileGenerator>> generatorList = buildGeneratorList();
	
	
	private static Map<String, Class<? extends FileGenerator>> buildGeneratorList(){
		SortedMap<String, Class<? extends FileGenerator>> result = new TreeMap<String, Class<? extends FileGenerator>>();
		result.put("Fichier avec template texte",TextGenerator.class);
		result.put("Fichier Excel(.xls)",XlsGenerator.class);
		result.put("Fichier avec template XML", EncodedXmlGenerator.class);
		result.put("Fichier texte",SimpleTxtGenerator.class);
		result.put("Fichier XML", XmlGenerator.class);
		result.put("Fichier GenCross-Drawer", DrawerGenerator.class);
		result.put("Fichier texte avec commentaires", SimpleTextWithCommentsGenerator.class);
		result.put("Fichier PowerPoint .pptx", PptxGenerator.class);
		result.put("Fichier texte compact", CompactFileGenerator.class);
		
		return result;
	}
		
	public static Map<String, Class<? extends FileGenerator>> getGeneratorList(){
		return generatorList;
	}
	
	public abstract void write(Personnage personnage, OutputStream outputStream) throws FileNotFoundException, IOException;
	
	public abstract String getOutputExtension();


}
