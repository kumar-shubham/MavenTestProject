package com.pisight.pimoney.beans;

import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncodeDecodeUtil {

	
	private static final String CIPHER_INSTANCE_NAME = "DES/ECB/PKCS5Padding";
    private static Key key = null;
    private static final String KEY_STR = "12fmykeypisight7972322*%gtnafnietl830#m";
    private static final String DES = "DES";
    private static final String ENCODE_TYPE = "UTF-8";
    
    private static Cipher cipher = null;

	static Key getKey() throws Exception {
        byte[] bytes = KEY_STR.getBytes();
        DESKeySpec pass = new DESKeySpec(bytes);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(DES);
        /*SecretKey s = skf.generateSecret(pass);
        return s;*/
        return skf.generateSecret(pass);
     }
		

    public static String encodeString(String inputStr) {
        String encodedStr = null;
        try {
            cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME);
            key = getKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            byte[] outputBytes = cipher.doFinal(inputStr.getBytes());
            Base64 encoder = new Base64();
            encodedStr = encoder.encodeBase64URLSafeString(outputBytes);
            encodedStr = URLEncoder.encode(encodedStr, ENCODE_TYPE);
        } catch (Exception e) {
           e  = null;
        }
        return encodedStr;
    }
    
}
