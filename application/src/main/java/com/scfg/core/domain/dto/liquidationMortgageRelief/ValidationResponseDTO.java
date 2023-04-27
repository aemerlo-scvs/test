package com.scfg.core.domain.dto.liquidationMortgageRelief;

import java.io.Serializable;

public class ValidationResponseDTO<T>  implements Serializable {

    private boolean caseInOrder;
    private String exclusionDescription;
    private T data;

    public ValidationResponseDTO() {
    }

    public ValidationResponseDTO(boolean caseInOrder, String exclusionDescription) {
        this.caseInOrder = caseInOrder;
        this.exclusionDescription = exclusionDescription;
    }

    public ValidationResponseDTO(boolean caseInOrder, String exclusionDescription, T data) {
        this.caseInOrder = caseInOrder;
        this.exclusionDescription = exclusionDescription;
        this.data = data;
    }

    public boolean isCaseInOrder() {
        return caseInOrder;
    }

    public ValidationResponseDTO setCaseInOrder(boolean caseInOrder) {
        this.caseInOrder = caseInOrder;
        return this;

    }

    public String getExclusionDescription() {
        return exclusionDescription;
    }

    public ValidationResponseDTO setExclusionDescription(String exclusionDescription) {
        this.exclusionDescription = exclusionDescription;
        return this;
    }

    public T getData() {
        return data;
    }

    public ValidationResponseDTO setData(T data) {
        this.data = data;
        return this;
    }
}
