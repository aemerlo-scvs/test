package com.scfg.core.common.exception;

public class OperationException extends RuntimeException {
    public OperationException(String message) {
        super(message);
    }

    public OperationException(String message, Throwable throwable) {
        super(message, throwable);
    }


    //#region Auxiliary methods

    public static void throwExceptionIfTextInvalid(String title, String value) {
        if (value == null || value.trim().length() == 0) {
            throw new OperationException("No se pudo realizar la operación, el campo " + title + " no debe estar vacio");
        }
    }

    public static void throwExceptionIfNumberInvalid(String title, Long value) {
        if (value == null)
            throw new OperationException("No se pudo realizar la operación, El campo '" + title + "' es requerido");
        if (value.compareTo(0L) <= 0) {
            throw new OperationException("No se pudo realizar la operación, El campo '" + title + "' debe ser mayor que 0");
        }
    }

    //#endregion
}
