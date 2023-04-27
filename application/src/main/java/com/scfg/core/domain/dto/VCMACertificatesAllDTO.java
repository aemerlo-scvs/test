package com.scfg.core.domain.dto;

import java.io.Serializable;

public class VCMACertificatesAllDTO implements Serializable {

    private Long fileDocumentId;
    private String fileDocumentName;
    private Long requestId;
    private String identificationNumber;

    public Long getFileDocumentId() {
        return fileDocumentId;
    }

    public void setFileDocumentId(Long fileDocumentId) {
        this.fileDocumentId = fileDocumentId;
    }

    public String getFileDocumentName() {
        return fileDocumentName;
    }

    public void setFileDocumentName(String fileDocumentName) {
        this.fileDocumentName = fileDocumentName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

}