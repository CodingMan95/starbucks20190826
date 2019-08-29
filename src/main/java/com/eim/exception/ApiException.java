package com.eim.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiException extends RuntimeException {
    protected int errorCode;
    protected Object data;

    public ApiException(int errorCode, String message, Object data, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
        this.data = data;
    }

    public ApiException(int errorCode, String message, Object data) {
        this(errorCode, message, data, null);
    }

    public ApiException(int errorCode, String message) {
        this(errorCode, message, null, null);
    }

    public ApiException(Throwable e) {
        super(e);
    }

}
