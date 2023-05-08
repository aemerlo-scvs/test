package com.scfg.core.application.service;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.scfg.core.application.port.in.JksCertificateUseCase;
import com.scfg.core.application.port.out.JksCertificatePort;
import com.scfg.core.common.enums.JksAliasEnum;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.Crypt;
import com.scfg.core.common.util.Jks;
import com.scfg.core.domain.JksCertificate;
import com.scfg.core.domain.dto.JksCertificateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JksCertificateService implements JksCertificateUseCase {

    private final JksCertificatePort jksCertificatePort;

    private String certKeyStore = "Scvs_d3s4rr0ll0";

    @Override
    public List<JksCertificateDTO> getAll() {
        return jksCertificatePort.findAllDTO();
    }

    @Override
    public JksCertificateDTO getById(Long id) {
        return jksCertificatePort.findByIdDTO(id);
    }

    @Override
    public Boolean saveOrUpdate(JksCertificateDTO jksCertificateDTO) {
        JksCertificate jksCertificate = new JksCertificate(jksCertificateDTO, certKeyStore);

        return jksCertificatePort.saveOrUpdate(jksCertificate) > 0;
    }

    @Override
    public String signDocumentWithP12Cert(String document, List<String> ownerSigns) {
        String signedDocument = "";
        String signedDocumentAux = "";

        if (document.isEmpty()) {
            throw new OperationException("El documento a firmar esta vacio");
        }

        List<JksCertificateDTO> jksCertificateDTOList = getJksCertificateDTOList(ownerSigns);


        for (int i = 0; i < jksCertificateDTOList.size(); i++) {
            if (i == 0) {
                signedDocumentAux = document;
            }

            signedDocumentAux = signInBase64(signedDocumentAux, jksCertificateDTOList.get(i));

            signedDocument = signedDocumentAux;
        }

        return signedDocument;
    }

    @Override
    public byte[] signDocumentWithP12Cert(byte[] document, List<String> ownerSigns) {
        byte[] signedDocument = null;
        byte[] signedDocumentAux = null;

        if (document != null) {
            throw new OperationException("El documento a firmar esta vacio");
        }

        List<JksCertificateDTO> jksCertificateDTOList = getJksCertificateDTOList(ownerSigns);

        for (int i = 0; i < jksCertificateDTOList.size(); i++) {
            if (i == 0) {
                signedDocumentAux = document;
            }

            signedDocumentAux = signInByte(signedDocumentAux, jksCertificateDTOList.get(i));

            signedDocument = signedDocumentAux;
        }

        return signedDocument;
    }


    //#region Auxiliary Methods

    private List<JksCertificateDTO> getJksCertificateDTOList(List<String> ownerSigns) {
        List<JksCertificateDTO> jksCertificateDTOList = new ArrayList<>();

        if (ownerSigns.size() == 0) {
            throw new OperationException("Indique al menos una firma con que la que se debe firmar el documento");
        }

        List<JksCertificate> jksCertificateList = jksCertificatePort.findAllByAbbreviations(ownerSigns);

        if (jksCertificateList.size() != ownerSigns.size()) {
            throw new OperationException("No se econtrarón los certificados de todas las firmas requeridas");
        }

        jksCertificateList.forEach(jksCertificate -> {
            try {
                String certPassword = Jks.getAliasValue(JksAliasEnum.CERT_PASSWORD.getValue(), certKeyStore, jksCertificate.getJks());
                String certContent = Jks.getAliasValue(JksAliasEnum.CERT_CONTENT.getValue(), certKeyStore, jksCertificate.getJks());

                JksCertificateDTO jksCertificateDTO = new JksCertificateDTO(jksCertificate, certPassword, certContent);
                jksCertificateDTOList.add(jksCertificateDTO);

            } catch (Exception ex) {
                throw new OperationException("El certificado de: " + jksCertificate.getAbbreviation() + ", no es valido");
            }
        });
        return jksCertificateDTOList;
    }

    private String signInBase64(String documentToSign, JksCertificateDTO cert) {

        byte[] documentToSignAux = Base64.getDecoder().decode(documentToSign);
        byte[] signedDocument = signInByte(documentToSignAux, cert);

        return Base64.getEncoder().encodeToString(signedDocument);
    }

    private byte[] signInByte(byte[] documentToSign, JksCertificateDTO cert) {

        byte[] signedDocument = null;
        try {
            KeyStore ks = KeyStore.getInstance("pkcs12");
            byte[] certDocumentDecoded = Base64.getDecoder().decode(cert.getContent());
            char[] certPasswordCharArray = cert.getPassword().toCharArray();

            InputStream is = new ByteArrayInputStream(certDocumentDecoded);

            ks.load(is, certPasswordCharArray);
            String alias = (String) ks.aliases().nextElement();
            PrivateKey key = (PrivateKey) ks.getKey(alias, certPasswordCharArray);
            Certificate[] chain = ks.getCertificateChain(alias);

            // Recibimos como parámetro de entrada el nombre del archivo PDF a firmar
            PdfReader reader = new PdfReader(documentToSign);

            ByteArrayOutputStream bout = new ByteArrayOutputStream(8192);

            // Añadimos firma al documento PDF
            PdfStamper stp = PdfStamper.createSignature(reader, bout, '\0');

            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
            sap.setReason("Firma PKCS12");
            sap.setLocation("Santa Cruz");
//
//            // Añade la firma visible. Podemos comentarla para que no sea visible.
//            sap.setVisibleSignature(new Rectangle(50, 50, 250, 80), 1, "sig");
//            sap.setAcro6Layers(true);

            stp.close();

            signedDocument = bout.toByteArray();

        } catch (Exception ex) {
            throw new OperationException("Error" + ex.getMessage());
        }
        return signedDocument;
    }

    //#endregion


    //#region Deprecated Methods

    public Boolean createCertificate() {

        String privateKey = "4606628";
        String urlCert = "/usr/local/share/ca-certificates/RAFAEL_FERNANDO_MOLINA_LIZARAZU.p12";

        JksCertificate jksCertificate = new JksCertificate();

        String passwordKeyStore = Crypt.getInstance().generateKey();
        Map<String, String> containJks = new HashMap<>();
        containJks.put("cert-document", this.convertToBase64(urlCert));
        containJks.put("cert-password", "4606628");

        byte[] jks = Jks.createKeyStoreTo("gonza", containJks);

        jksCertificate.setId(0L);
        jksCertificate.setDescription("Certificado - rfmolina");
        jksCertificate.setJks(jks);

        return jksCertificatePort.saveOrUpdate(jksCertificate) > 0;
    }

    private String convertToBase64(String path) {
        File file = new File(path);
        byte[] fileArray = new byte[(int) file.length()];
        InputStream inputStream;

        String encodedFile = "";
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(fileArray);
            encodedFile = Base64.getEncoder().encodeToString(fileArray);
        } catch (Exception e) {
            // Manejar Error
        }
        return encodedFile;
    }


    //#endregion

}
