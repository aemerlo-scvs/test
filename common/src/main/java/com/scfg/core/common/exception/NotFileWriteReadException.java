package com.scfg.core.common.exception;

import java.io.IOException;

public class NotFileWriteReadException  extends RuntimeException {
    public NotFileWriteReadException(String message) {
        super(message);
    }

    public NotFileWriteReadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
