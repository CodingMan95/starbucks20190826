package com.eim.exception;

import com.eim.kit.ConstantKit;

public class BusinessException extends ApiException {

    public BusinessException(String message) {
        super(ConstantKit.BAD_REQUEST, message, null);
    }

    public BusinessException(int errCode, String message) {
        super(errCode, message, null);
    }
}
