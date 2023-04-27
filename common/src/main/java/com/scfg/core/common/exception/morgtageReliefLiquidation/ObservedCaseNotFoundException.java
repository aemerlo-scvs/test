package com.scfg.core.common.exception.morgtageReliefLiquidation;

public class ObservedCaseNotFoundException extends RuntimeException {


    public ObservedCaseNotFoundException(String message) {
        super(message);
    }

    public ObservedCaseNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
