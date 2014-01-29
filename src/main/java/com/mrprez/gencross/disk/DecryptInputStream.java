package com.mrprez.gencross.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DecryptInputStream extends CipherInputStream {

	private DecryptInputStream(InputStream is, Cipher c) {
		super(is, c);
	}
	
	public static DecryptInputStream buildDecryptInputStream(InputStream is) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException{
		Cipher cipher = Cipher.getInstance("DES");
    	KeySpec key = new DESKeySpec("wofvklme".getBytes());
    	cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(key));
    	return new DecryptInputStream(is, cipher);
	}
	
	public static DecryptInputStream buildDecryptInputStream(File file) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, FileNotFoundException{
		return buildDecryptInputStream(new FileInputStream(file));
	}
		

}
