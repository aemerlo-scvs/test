package com.scfg.core.common.exception.morgtageReliefLiquidation;

public class CreditOperationNotFoundException extends RuntimeException {


    public CreditOperationNotFoundException(String message) {
        super(message);
    }

    public CreditOperationNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
