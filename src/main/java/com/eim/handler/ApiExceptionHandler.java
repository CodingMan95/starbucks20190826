package com.eim.handler;

import com.eim.exception.ApiException;
import com.eim.model.ErrorTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    /**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorTemplate> exception(Exception exception, HttpServletResponse response) {
        ErrorTemplate errorDTO = new ErrorTemplate();
        if (exception instanceof ApiException) {//api异常
            ApiException apiException = (ApiException) exception;
            errorDTO.setErrorCode(apiException.getErrorCode());
        } else {//未知异常
            errorDTO.setErrorCode(500);
        }
        errorDTO.setMessage(exception.getMessage());
        errorDTO.setStatus(false);
        ResponseEntity<ErrorTemplate> responseEntity = new ResponseEntity<>(errorDTO, HttpStatus.valueOf(response.getStatus()));
        return responseEntity;
    }

}
