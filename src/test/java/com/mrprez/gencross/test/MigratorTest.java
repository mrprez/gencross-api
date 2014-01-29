package com.mrprez.gencross.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.disk.PersonnageFactory;
import com.mrprez.gencross.disk.PersonnageSaver;

public abstract class MigratorTest {
	private String fileName;
	private PersonnageFactory personnageFactory;
	public static final String REF_SUFFIX = "Ref";
	public static final String RESULT_SUFFIX = "Result";
	public static final String XML_EXTENSION = ".xml";
	
	
	public MigratorTest(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	@Before
	public void setUp() throws Exception{
		personnageFactory = new PersonnageFactory(true);
	}
	
	
	@Test
	public void test() throws Exception {
		Personnage personnage = personnageFactory.loadPersonnage(ClassLoader.getSystemResourceAsStream(fileName));
		PersonnageSaver.savePersonnageXml(personnage, getResultFile());
		
		Assert.assertTrue(getRefFile().getAbsolutePath()+" does not exist", getRefFile().exists());
		
		compareFiles(getRefFile(), getResultFile());
		
	}
	
	private void compareFiles(File expectedFile, File actualFile) throws IOException{
		BufferedReader expectedReader = new BufferedReader(new FileReader(expectedFile));
		try{
			BufferedReader actualReader = new BufferedReader(new FileReader(actualFile));
			try{
				String expectedLine;
				String actualLine;
				while((expectedLine=expectedReader.readLine()) != null
						&& (actualLine=actualReader.readLine()) != null){
					Assert.assertEquals(expectedLine, actualLine);
				}
				Assert.assertFalse(actualReader.ready());
				Assert.assertFalse(expectedReader.ready());
				
			}finally{
				actualReader.close();
			}
		}finally{
			expectedReader.close();
		}
	}
	
	private File getRefFile(){
		String fileAbsoluteName = ClassLoader.getSystemResource(fileName).getFile();
		String base = fileAbsoluteName.substring(0, fileAbsoluteName.length() - XML_EXTENSION.length());
		return new File(base + REF_SUFFIX + XML_EXTENSION);
	}
	
	private File getResultFile(){
		String fileAbsoluteName = ClassLoader.getSystemResource(fileName).getFile();
		String base = fileAbsoluteName.substring(0, fileAbsoluteName.length() - XML_EXTENSION.length());
		return new File(base + RESULT_SUFFIX + XML_EXTENSION);
	}
	
	

}
