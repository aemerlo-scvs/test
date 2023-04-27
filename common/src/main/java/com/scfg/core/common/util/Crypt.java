package com.scfg.core.common.util;


import lombok.extern.slf4j.Slf4j;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

@Slf4j
public class Crypt {

    private final Random secureRandom;
    private static final Crypt crypt = new Crypt();

    private Crypt() {
        secureRandom = new SecureRandom();
    }

    public static Crypt getInstance() {
        return crypt;
    }

    public String crypt(String textPlain, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(textPlain.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String crypt(String textPlain) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            String secretKey = this.generateKey();
            SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String ciphered = Base64.getEncoder().encodeToString(cipher.doFinal(textPlain.getBytes(StandardCharsets.UTF_8)));
            return secretKey + ciphered;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String cipheredInBase64) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            String secretKey = cipheredInBase64.substring(0, 22);
            cipheredInBase64 = cipheredInBase64.substring(22);

            SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode = Base64.getDecoder().decode(cipheredInBase64);
            byte[] ciphered = cipher.doFinal(decode);
            return new String(ciphered, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String cipheredInBase64, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decode = Base64.getDecoder().decode(cipheredInBase64);
            byte[] ciphered = cipher.doFinal(decode);
            return new String(ciphered, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateKey() {
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public byte[] generatePin(Integer len) {
        byte[] key = new byte[len];
        secureRandom.nextBytes(key);
        for (int i = 0; i < len; i++) {
            key[i] = (byte) secureRandom.nextInt(10);
            // log.info(key[i] + "");
        }
        return key;
    }

    public String generateKey(int nChars) {
        int bytes = (int) ((double) (nChars * 6) / 8 + 0.49);
        byte[] key = new byte[bytes];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public String md5(String data)
            throws NoSuchAlgorithmException {
        // Get the algorithm:
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // Calculate Message Digest as bytes:
        byte[] digest = md5.digest(data.getBytes(StandardCharsets.UTF_8));
        // Convert to 32-char long String:
        return String.format("%032x%n", new BigInteger(1, digest));
    }
}
