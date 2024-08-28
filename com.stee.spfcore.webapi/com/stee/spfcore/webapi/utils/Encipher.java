package com.stee.spfcore.webapi.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encipher {

	private static final Logger logger = LoggerFactory.getLogger(Encipher.class);
	
	private static final String UNICODE_FORMAT = "UTF-8";
	//private static final String DESEDE_ENCRYPTION_SCHEME = "DESede/CBC/PKCS5Padding";
	//private static final String DESEDE_KEY_SCHEME = "DESede";
	//private static final String AES_ENCRYPTION_SCHEME = "AES/CBC/PKCS5Padding";
	private static final String AES_ENCRYPTION_SCHEME = "AES/GCM/NoPadding";
	private static final String AES_KEY_SCHEME = "AES";
	private static final String AES_HASH_ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int AES_ITERATION_COUNT = 65536;
	private static final int AES_KEY_LENGTH = 128; //limited to 128, install JCE for 256
	private static final int TAG_BIT_LENGTH = 128;
	
	private static byte[] keyiv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
									0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

	private Cipher cipher;
	private SecretKey key;
	//private IvParameterSpec paramSpec;
	private GCMParameterSpec paramSpec;

	public Encipher (String encryptionKey) throws UnsupportedEncodingException, GeneralSecurityException {
		
		String encryptionScheme = AES_ENCRYPTION_SCHEME;
		byte[] arrayBytes = encryptionKey.getBytes(UNICODE_FORMAT);

		//paramSpec = new IvParameterSpec(keyiv);
		paramSpec = new GCMParameterSpec(TAG_BIT_LENGTH, keyiv);
		
		KeySpec keySpec = new PBEKeySpec(encryptionKey.toCharArray(), arrayBytes, AES_ITERATION_COUNT, AES_KEY_LENGTH);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(AES_HASH_ALGORITHM);
		SecretKey tmp = secretKeyFactory.generateSecret(keySpec);
		
		this.cipher = Cipher.getInstance(encryptionScheme);
		this.key = new SecretKeySpec(tmp.getEncoded(), AES_KEY_SCHEME);
	}

	public String encrypt(String unencryptedString) {
		
		String encryptedString = null;

		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, this.key, paramSpec);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = this.cipher.doFinal(plainText);

			encryptedString = new String(Base64.encodeBase64(encryptedText), Charset.forName(UNICODE_FORMAT));
		} 
		catch (GeneralSecurityException | UnsupportedEncodingException  exception) {
			logger.warn("Unable to encrpt the string.", exception);
		}

		return encryptedString;
	}

	public String decrypt(String encryptedString) {
		
		String decryptedText = null;

		try {
			this.cipher.init(Cipher.DECRYPT_MODE, this.key, paramSpec);
			byte[] encryptedText = Base64.decodeBase64(encryptedString);
			byte[] plainText = this.cipher.doFinal(encryptedText);

			decryptedText = new String(plainText, Charset.forName(UNICODE_FORMAT));
		} 
		catch (GeneralSecurityException  exception) {
			logger.warn("Unable to decrypt the string.", exception);
		}

		return decryptedText;
	}
}