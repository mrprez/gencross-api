package com.mrprez.gencross.disk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.mrprez.gencross.Personnage;

public class PersonnageSaver {
	
	public static void savePersonnage(Personnage personnage, File file) throws FileNotFoundException, IOException, GeneralSecurityException{
		if(file.getName().endsWith(".xml")){
			savePersonnageXml(personnage, file);
		}else{
			savePersonnageGcr(personnage, file);
		}
	}
	
	public static void savePersonnageXml(Personnage personnage, File file) throws FileNotFoundException, IOException{
		savePersonnage(personnage.getXML(), new FileOutputStream(file));
	}
	
	public static void savePersonnageGcr(Personnage personnage, OutputStream os) throws FileNotFoundException, IOException, GeneralSecurityException {
		savePersonnageGcr(personnage.getXML(), os);
	}
	
	public static void savePersonnageGcr(Personnage personnage, File file) throws FileNotFoundException, IOException, GeneralSecurityException, GeneralSecurityException{
		savePersonnageGcr(personnage.getXML(), new FileOutputStream(file));
	}
	
	public static void savePersonnageGcr(Document personnageXml, OutputStream os) throws FileNotFoundException, IOException, GeneralSecurityException, GeneralSecurityException{
		Cipher cipher = Cipher.getInstance("DES");
    	KeySpec key = new DESKeySpec("wofvklme".getBytes());
    	cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(key));
    	CipherOutputStream cipherOutputStream = new CipherOutputStream(os, cipher);
    	savePersonnage(personnageXml, cipherOutputStream);
	}
	
	public static void savePersonnage(Document personnageXml, OutputStream os) throws IOException{
		XMLWriter writer = new XMLWriter(os, new OutputFormat("\t", true, "UTF-8"));
		try{
			writer.write(personnageXml);
		}finally{
			writer.close();
		}
	}

	public static void savePersonnage(Personnage personnage, OutputStream os) throws IOException {
		savePersonnage(personnage.getXML(), os);
	}

}
