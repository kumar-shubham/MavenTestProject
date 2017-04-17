/**
 * 
 */
package com.pisight.pimoney1.beans;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author abhijitpetkar
 *
 */
public class EncodeDecodeUtil {

	private static final String CIPHER_INSTANCE_NAME = "DES/ECB/PKCS5Padding";

	private static Key key = null;

	private static Cipher cipher = null;

	private static final String KEY_STR = "12fmykeypisightaca20121991*%gtnafnietl830#m";

	private static final String DES = "DES";

	private static final String ENCODE_TYPE = "UTF-8";

	private static final String NOT_AUTHORIZED = "not authorized";

	private static final String VALID = "valid";


	static Key getKey() throws Exception {
		byte[] bytes = KEY_STR.getBytes();
		DESKeySpec pass = new DESKeySpec(bytes);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(DES);
		return skf.generateSecret(pass);
	}

	public static String encodeString(String inputStr) {
		String encodedStr = null;
		try {
			cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
			key = getKey();
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] outputBytes = cipher.doFinal(inputStr.getBytes());
			encodedStr = Base64.encodeBase64URLSafeString(outputBytes);
			encodedStr = URLEncoder.encode(encodedStr, ENCODE_TYPE);
		} catch (Exception e) {
			e = null;
		}
		return encodedStr;
	}

	public static String decodeString(String encodedStr) {
		String decodeStr = null;
		try {
			encodedStr = URLDecoder.decode(encodedStr, ENCODE_TYPE);
			byte encrypted[] = Base64.decodeBase64(encodedStr);
			cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
			key = getKey();
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] outputBytes = cipher.doFinal(encrypted);
			String decodedCN = new String(outputBytes);
			decodeStr =  decodedCN ;
		} catch (Exception e) {
			e = null;
		}
		return decodeStr;
	}



	//AES Based Implementation

	private static String salt = "TheP!m0neyS@lt";
	private static int iterations = 65536;
	private static int keySize = 256;
	private static byte[] ivBytes;
	private static Cipher encryptCipher;

	private static SecretKey secretKey;
	
	private static final int IV_LENGTH = 16;

	static {
		try {
			secretKey = getAESKey();
			ivBytes = getIVBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String plaintext) throws Exception {

		byte[] encryptedTextBytes = encryptCipher.doFinal(plaintext.getBytes("UTF-8"));
		
		byte[] finalTextBytes = new byte[ivBytes.length + encryptedTextBytes.length];
		System.arraycopy(ivBytes, 0, finalTextBytes, 0, ivBytes.length);
	    System.arraycopy(encryptedTextBytes, 0, finalTextBytes, ivBytes.length, encryptedTextBytes.length);

		return DatatypeConverter.printBase64Binary(finalTextBytes);
	}

	public static String decrypt(String encryptedText) throws Exception {
		
		if (encryptedText.length() <= IV_LENGTH) {
	        throw new Exception("The input string is not long enough to contain the initialisation bytes and data.");
	    }
		
		byte[] byteArray = DatatypeConverter.parseBase64Binary(encryptedText);
	    byte[] ivBytes = new byte[IV_LENGTH];
	    System.arraycopy(byteArray, 0, ivBytes, 0, 16);
	    byte[] encryptedTextBytes = new byte[byteArray.length - ivBytes.length];
	    System.arraycopy(byteArray, IV_LENGTH, encryptedTextBytes, 0, encryptedTextBytes.length);

		SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes));

		byte[] decryptedTextBytes = null;

		try {
			decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return new String(decryptedTextBytes);
	}

	public static byte[] getSaltByte() throws Exception {
		return salt.getBytes("UTF-8");
	}
	
	static SecretKey getAESKey() throws Exception {
		byte[] saltBytes = getSaltByte();

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(KEY_STR.toCharArray(), saltBytes, iterations, keySize);
		return skf.generateSecret(spec);
	}
	
	static byte[] getIVBytes() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException {
		SecretKeySpec secretSpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretSpec);
		AlgorithmParameters params = cipher.getParameters();
		encryptCipher = cipher;
		return params.getParameterSpec(IvParameterSpec.class).getIV();
	}


	public static SecretKey getSecretEncryptionKey() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(256); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		return secKey;
	}

	public static String encryptText(String plainText, SecretKey secKey) throws Exception {

		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, secKey);

		byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
		return new String(byteCipherText);

	}

	public static String decryptText(String byteCipherText, SecretKey secKey) throws Exception {
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.DECRYPT_MODE, secKey);

		byte[] bytePlainText = aesCipher.doFinal(byteCipherText.getBytes());
		return new String(bytePlainText);
	}

	public static String generateAccessToken(@PathVariable("userID") String userID, @PathVariable("timestamp") String timestamp) {

		String accessToken = "";

		if (userID != null && timestamp != null) {
			StringBuffer stringBuffer = new StringBuffer(userID);
			stringBuffer.append(timestamp);

			accessToken = EncodeDecodeUtil.encodeString(stringBuffer.toString());
		}

		return accessToken;
	}

	public static String validateAccessToken(String userId, String timestamp, String accessToken) {

		String validationStatus = VALID;

		String localGeneratedToken = generateAccessToken(userId, timestamp);
		if (localGeneratedToken == null || !localGeneratedToken.equals(accessToken)) {
			validationStatus = NOT_AUTHORIZED;
		}
		return validationStatus;
	}
}
