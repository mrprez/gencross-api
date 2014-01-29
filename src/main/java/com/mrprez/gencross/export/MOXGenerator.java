package com.mrprez.gencross.export;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.mrprez.gencross.Personnage;

public abstract class MOXGenerator extends TemplatedFileGenerator {
	private static long exportNumber = 0;
	private ZipFile template;
	private File directory;
	private SAXReader saxReader = new SAXReader();
	private int compressionMethod;
	

	@Override
	public void setTemplate(File template) throws FileNotFoundException, IOException {
		this.template = new ZipFile(template);
		directory = new File(template.getParentFile(), template.getName().substring(0, template.getName().indexOf("."))+getExportNumber());
		directory.mkdir();
	}

	@Override
	public void write(Personnage personnage, OutputStream outputStream) throws FileNotFoundException, IOException {
		try {
			extractTemplate();
			treatFile(directory, personnage);
			compressDirectory(outputStream);
			deleteFile(directory);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private static synchronized long getExportNumber(){
		exportNumber++;
		return exportNumber;
	}
	
	
	private void treatFile(File file, Personnage personnage) throws DocumentException, IOException{
		if(file.isDirectory()){
			File subFiles[] = file.listFiles();
			for(int i=0; i<subFiles.length; i++){
				treatFile(subFiles[i], personnage);
			}
		}else if(file.isFile()){
			if(file.getName().endsWith(".xml") || file.getName().endsWith(".xml.rels")){
				Document document = saxReader.read(file);
				treatDocument(document, personnage);
				OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
				OutputFormat outputFormat = new OutputFormat("", false, "UTF-8");
				outputFormat.setLineSeparator("\r\n");
				XMLWriter writer = new XMLWriter(os, outputFormat);
				writer.write(document);
			}
		}
		
	}
	
	private Document treatDocument(Document document, Personnage personnage) throws DocumentException{
		treatElement(document.getRootElement(), personnage);
		return document;
	}
	private Element treatElement(Element element, Personnage personnage){
		if(element.getTextTrim()!=null){
			if(element.getTextTrim().startsWith("#")){
				String text = element.getTextTrim();
				text = text.substring(1);
				if(text.endsWith("#")){
					text = text.substring(0, text.length()-1);
				}
				text = getText(properties.getProperty(text, text), personnage);
				element.setText(text);
			}
		}
		for(Object subElement : element.elements()){
			treatElement((Element)subElement, personnage);
		}
		return element;
	}
	
	private void compressDirectory(OutputStream outputStream) throws IOException{
		ZipOutputStream zos = new ZipOutputStream(outputStream);
		zos.setMethod(compressionMethod);
		compress(directory, zos);
		zos.close();
	}
	
	private void compress(File file, ZipOutputStream zos) throws IOException{
		if(file.isDirectory()){
			for(File subFile : file.listFiles()){
				compress(subFile, zos);
			}
		}else if(file.isFile()){
			String entryName = file.getAbsolutePath().substring(directory.getAbsolutePath().length());
			while(entryName.startsWith(File.separator)){
				entryName = entryName.substring(1);
			}
			ZipEntry zipEntry = new ZipEntry(entryName);
			zos.putNextEntry(zipEntry);
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
			int bufferLength = 2048;
			byte data[] = new byte[bufferLength];
			int count;
			while((count = is.read(data, 0, bufferLength)) >= 0) {
				zos.write(data, 0, count);
			}
			zos.closeEntry();
			is.close();
		}
		
		
	}
	
	private void extractTemplate() throws IOException{
		Enumeration<? extends ZipEntry> entries = template.entries();
		while(entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry)entries.nextElement();
			File newFile = createFile(entry.getName());
			extractEntry(entry, newFile);
		}
		
	}
	
	private File createFile(String absolutePath){
		String path[] = absolutePath.split("[/]");
		File currentFile = directory;
		for(int i=0; i<path.length-1; i++){
			currentFile = new File(currentFile, path[i]);
			if(!currentFile.exists()){
				currentFile.mkdir();
			}
		}
		File newFile = new File(currentFile, path[path.length-1]);
		return newFile;
	}
	
	private void extractEntry(ZipEntry zipEntry, File newFile) throws IOException{
		compressionMethod = zipEntry.getMethod();
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(newFile));
		BufferedInputStream is = new BufferedInputStream(template.getInputStream(zipEntry));
		int count;
		int bufferLength = 2048;
		byte data[] = new byte[bufferLength];
		while((count = is.read(data, 0, bufferLength))>=0){
			os.write(data, 0, count);
		}
		os.close();
	}
	
	private void deleteFile(File file){
		if(file.isDirectory()){
			for(File subFile : file.listFiles()){
				deleteFile(subFile);
			}
		}
		boolean success = file.delete();
		if(!success){
			System.out.println("Cannot delete "+file.getAbsolutePath());
		}
	}

}
