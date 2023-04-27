package com.scfg.core.common.exception.morgtageReliefLiquidation;

import lombok.Getter;
import lombok.Setter;

public class ClassifierNotFoundException extends RuntimeException {

    private String classifierName;

    public ClassifierNotFoundException(String message, String classifierName) {
        super(message);
        this.classifierName = classifierName;
    }

    public ClassifierNotFoundException(String message, Throwable throwable, String classifierName) {
        super(message, throwable);
        this.classifierName = classifierName;
    }


    public String getClassifierName() {
        return classifierName;
    }
}
