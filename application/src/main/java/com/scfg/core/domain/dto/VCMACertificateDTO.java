package com.scfg.core.domain.dto;

import java.io.Serializable;

public class VCMACertificateDTO implements Serializable {

    private Long requestId;
    private Long sectionId;
    private Long subSectionId;
    private Long endorsementId;
    private String policyNumber;
    private String holderCompleteName;
    private String identificationNumber;
    private Long fileDocumentId;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getSubSectionId() {
        return subSectionId;
    }

    public void setSubSectionId(Long subSectionId) {
        this.subSectionId = subSectionId;
    }

    public Long getEndorsementId() {
        return endorsementId;
    }

    public void setEndorsementId(Long endorsementId) {
        this.endorsementId = endorsementId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getHolderCompleteName() {
        return holderCompleteName;
    }

    public void setHolderCompleteName(String holderCompleteName) {
        this.holderCompleteName = holderCompleteName;
    }

    public Long getFileDocumentId() {
        return fileDocumentId;
    }

    public void setFileDocumentId(Long fileDocumentId) {
        this.fileDocumentId = fileDocumentId;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
}