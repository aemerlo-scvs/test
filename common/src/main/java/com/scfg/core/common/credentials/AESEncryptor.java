package com.scfg.core.common.credentials;

import org.jasypt.encryption.StringEncryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESEncryptor implements StringEncryptor {

    String secretKey;
    Boolean isSecretKeyBase64;
    String iv;
    String encryptAlgorithm;
    String decryptAlgorithm;

    @Override
    public String encrypt(String message) {
        try {

            byte[] secretKeyAux = (isSecretKeyBase64) ? Base64.getDecoder().decode(secretKey.getBytes()) : secretKey.getBytes();

            Cipher cipher = Cipher.getInstance(encryptAlgorithm);
            SecretKeySpec secretKeyTemp = new SecretKeySpec(secretKeyAux, "AES");

            IvParameterSpec newIv = (this.iv.isEmpty()) ? generateIv() : generateIvBase64(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeyTemp, newIv);
            byte[] cipherText = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public String decrypt(String encryptedMessage) {
        try {

            byte[] secretKeyAux = (isSecretKeyBase64) ? Base64.getDecoder().decode(secretKey.getBytes()) : secretKey.getBytes();

            Cipher cipher = Cipher.getInstance(decryptAlgorithm);
            SecretKeySpec secretKeyTemp = new SecretKeySpec(secretKeyAux, "AES");

            IvParameterSpec newIv = (this.iv.isEmpty()) ? generateIv() : generateIvBase64(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeyTemp, newIv);
            String decryptedPass = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
            decryptedPass =  cleanMessage(decryptedPass);
            return decryptedPass.replaceAll("\\P{Print}", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        return new IvParameterSpec(iv);
    }

    private IvParameterSpec generateIvBase64(String ivBase64) {
        byte[] ivAux = Base64.getDecoder().decode(ivBase64.getBytes());
        return new IvParameterSpec(ivAux);
    }

    private String cleanMessage(String message) {
        int count = 0;

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c > 127) {
                count = i;
                break;
            }
        }

        if(count > 0){
            message = message.substring(0, count - 1);
        }

        return message;
    }
}
