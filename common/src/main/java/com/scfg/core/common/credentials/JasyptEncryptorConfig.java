package com.scfg.core.common.credentials;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class JasyptEncryptorConfig {

    @Profile({"dev", "prod"})
    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor passwordEncryptor() {

        AESEncryptor encryptor = new AESEncryptor();
        encryptor.encryptAlgorithm = "AES/CBC/PKCS5Padding";
        encryptor.decryptAlgorithm = "AES/CBC/NoPadding";
        encryptor.secretKey = "ff557423aa1979799850ebfd56317f28";
        encryptor.isSecretKeyBase64 = false;
        encryptor.iv = "";

        return encryptor;
    }

    @Profile(value="pre-prod")
    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor passwordEncryptorPreProd() {

        AESEncryptor encryptor = new AESEncryptor();
        encryptor.encryptAlgorithm = "AES/CBC/PKCS5Padding";
        encryptor.decryptAlgorithm = "AES/CBC/NoPadding";
        encryptor.secretKey = "m+Y3I5C5enJtLgDeS/v44Ipoy+m4sGBiMg0uCRCBCJI=";
        encryptor.isSecretKeyBase64 = true;
        encryptor.iv = "xbdxvcQq4kw17AV3c2sx6Q==";

        return encryptor;
    }
}
