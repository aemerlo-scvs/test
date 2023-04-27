package com.scfg.core.common.util;

import com.scfg.core.common.exception.OperationException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Slf4j
public class Jks {
    public static String getAliasValue(String alias, String keyStorePassword, byte[] jksBytes) throws Exception{
        if (jksBytes == null) {
            // log.error("[{}] El arreglo de byte de JKS es nulo, revise con el administrador las causa.", alias);
            throw new OperationException("El arreglo de byte de JKS es nulo");
        }
        KeyStore ks;
        try {
            ks = KeyStore.getInstance("JCEKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jksBytes);
            ks.load(inputStream, keyStorePassword.toCharArray());

            KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
            KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry)ks.getEntry(alias, keyStorePP);
            PBEKeySpec keySpec = (PBEKeySpec)factory.getKeySpec(ske.getSecretKey(), PBEKeySpec.class);

            char[] password = keySpec.getPassword();
            return new String(password);
        }catch (KeyStoreException e) {
            // log.error("[{}] Error KeyStoreException al obtener certificado", alias, e);
            throw new OperationException("Error KeyStoreException  al obtener el certificado");
        } catch (NoSuchAlgorithmException e) {
            // log.error("[{}] Error NoSuchAlgorithmException al obtener certificado", alias, e);
            throw new OperationException("Error NoSuchAlgorithmException  al obtener el certificado");
        } catch (IOException e) {
            // log.error("[{}] Error IOException al obtener certificado", alias, e);
            throw new OperationException("Error IOException  al obtener el certificado");
        } catch (UnrecoverableKeyException e) {
            // log.error("[{}] Error UnrecoverableKeyException al obtener certificado", alias, e);
            throw new OperationException("Error UnrecoverableKeyException  al obtener el certificado");
        }
    }

    public static byte[] createKeyStoreTo(String passwordStr, Map<String, String> values) {
        try {
            final char[] password = passwordStr.toCharArray();
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(null, password);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
            for (Map.Entry<String, String> entry : values.entrySet()) {
                SecretKey generatedSecret = factory.generateSecret(new PBEKeySpec(entry.getValue().toCharArray(), "salt1".getBytes(), 1));
                ks.setEntry(entry.getKey(), new KeyStore.SecretKeyEntry(generatedSecret) , new KeyStore.PasswordProtection(password));
            }
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            ks.store(fos, password);
            return fos.toByteArray();
        } catch (KeyStoreException e) {
            // log.error("Error KeyStoreException al crear JKS", e);
            throw new OperationException("Error KeyStoreException al crear JKS");
        } catch (CertificateException e) {
            // log.error("Error CertificateException al crear JKS", e);
            throw new OperationException("Error CertificateException al crear JKS");
        } catch (NoSuchAlgorithmException e) {
            // log.error("Error NoSuchAlgorithmException al crear  jks", e);
            throw new OperationException("Error NoSuchAlgorithmException al crear  JKS");
        } catch (IOException e) {
            // log.error("Error IOException  al crear  jks", e);
            throw new OperationException("Error IOException  al crear  JKS");
        } catch (InvalidKeySpecException e) {
            // log.error("Error InvalidKeySpecException  al crear  jks", e);
            throw new OperationException("Error InvalidKeySpecException  al crear  JKS");
        }
    }

    public static byte[] updateValueEntry(String keyStorePassword, String alias, byte[] jksBytes, String newValue)throws Exception {
        if (jksBytes == null) {
            // log.error("[{}] El arreglo de byte de JKS es nulo, revise con el administrador las causa.", alias);
            throw new OperationException("El arreglo de byte de JKS es nulo");
        }
        KeyStore ks;
        try {
            final char[] password = keyStorePassword.toCharArray();
            ks = KeyStore.getInstance("JCEKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jksBytes);
            ks.load(inputStream, password);
            ks.deleteEntry(alias);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
            SecretKey generatedSecret = factory.generateSecret(new PBEKeySpec(newValue.toCharArray(), "salt1".getBytes(), 1));
            ks.setEntry(alias, new KeyStore.SecretKeyEntry(generatedSecret) , new KeyStore.PasswordProtection(password));

            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            ks.store(fos, password);
            return fos.toByteArray();
        }catch (KeyStoreException e) {
            // log.error("[{}] Error KeyStoreException al obtener certificado", alias, e);
            throw new OperationException("Error KeyStoreException  al obtener el certificado");
        } catch (NoSuchAlgorithmException e) {
            // log.error("[{}] Error NoSuchAlgorithmException al obtener certificado", alias, e);
            throw new OperationException("Error NoSuchAlgorithmException  al obtener el certificado");
        } catch (IOException e) {
            // log.error("[{}] Error IOException al obtener certificado", alias, e);
            throw new OperationException("Error IOException  al obtener el certificado");
        }
    }

}
