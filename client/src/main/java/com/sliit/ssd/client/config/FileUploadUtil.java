package com.sliit.ssd.client.config;

import java.io.*;
import java.nio.file.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.IOUtils;

 
public class FileUploadUtil {
	
	private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    /*
     * Getting a 128 bit key and iv for encryption
     */
    
    public static InputStream encriptFile(InputStream inputFile) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        
        byte[] nonEncryptedByteArray = IOUtils.toByteArray(inputFile);
        
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec secretkey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); //Cipher instance using AES encryption algorithm
        cipher.init(Cipher.ENCRYPT_MODE, secretkey, iv);
        byte[] encryptedByteArray = cipher.doFinal(nonEncryptedByteArray);
        
        /*
         * Used the cipher library to encrypt the stream to a byte array
         */
        InputStream encryptedInputStream = new ByteArrayInputStream(encryptedByteArray);
        
        /*
         * Back to streams, but this time encrypted
         */
        
        return encryptedInputStream;
    }
    
    public static InputStream decriptFile(InputStream inputFile) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        
        byte[] encrytToDecryptByteArray = IOUtils.toByteArray(inputFile);
        
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec secretkey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretkey, iv);
        byte[] decryptedByteArray = cipher.doFinal(encrytToDecryptByteArray);
        
        /*
         * dencrypted the encrypted data
         */
        
        InputStream decryptedInputStream = new ByteArrayInputStream(decryptedByteArray);
        
        return decryptedInputStream;
    }
     
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

         
        try {
        	InputStream inputStream = multipartFile.getInputStream();
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
			FileOutputStream fos = new FileOutputStream(uploadDir+"/" +fileName);

			encrypt(key, inputStream, fos);
        } catch (Exception ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        } catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
    }
    
    public static java.io.FileInputStream downloadFile(String uploadDir, String fileName) throws IOException {
		  java.io.FileInputStream fileInputStream = new java.io.FileInputStream(uploadDir + "/" + fileName);

		return fileInputStream;

    }
    
    
    
	public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}

	public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}
	
	public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}
	

	public static void doCopy(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
	}
 
    
}