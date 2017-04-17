package com.pisight.pimoney1.beans;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class EncryptionDecryption {

	private static String salt;
	private static int iterations = 65536  ;
	private static int keySize = 256;
	private static byte[] ivBytes;

	private static SecretKey secretKey;

	public static void main(String []args) throws Exception {

		salt = getSalt();
		
		long start = System.currentTimeMillis();

//		char[] message = "PasswordToEncrypt".toCharArray();
//		System.out.println("Message: " + String.valueOf(message));
//		System.out.println("Encrypted: " + encrypt(message));
//		System.out.println("Time taken to encrypt -> " + (System.currentTimeMillis() - start) + " mili second");
//		start = System.currentTimeMillis();
//		System.out.println("Decrypted: " + decrypt(encrypt(message).toCharArray()));
//		System.out.println("Time taken to decrypt -> " + (System.currentTimeMillis() - start) + " mili second");
		
		String s1 = EncodeDecodeUtil.encrypt("kumar");
//		String s1 = "izDgeaWavdIq302VVSynKqAv0IPnNtbYhaMb3FUFQrU=";
//		s1 = "uiNQScZwpNIoY57pSKMebuj0U2Qg9VMd6loqIu2uyEs=";
		System.out.println("S1 -.> " + s1);
		String s2 = EncodeDecodeUtil.decrypt(s1);
		System.out.println("S2 -.> " + s2);
		
	}

	public static String encrypt(char[] plaintext) throws Exception {
		byte[] saltBytes = salt.getBytes();

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(plaintext, saltBytes, iterations, keySize);
		secretKey = skf.generateSecret(spec);
		SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
		AlgorithmParameters params = cipher.getParameters();
		ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
		
		System.out.println("Secret key -> " + secretKey.getEncoded());
		System.out.println("IV Bytes     -> " + ivBytes);
		byte[] encryptedTextBytes = cipher.doFinal(String.valueOf(plaintext).getBytes("UTF-8"));

		return DatatypeConverter.printBase64Binary(encryptedTextBytes);
	}

	public static String decrypt(char[] encryptedText) throws Exception {

		System.out.println(encryptedText);

		byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(new String(encryptedText));
		SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

		byte[] decryptedTextBytes = null;

		try {
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		}   catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}   catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(decryptedTextBytes);

	}

	public static String getSalt() throws Exception {

		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[20];
		sr.nextBytes(salt);
		return new String(salt);
	}

}
