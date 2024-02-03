package com.back.util.exception;

import com.back.util.response.ResponseBodyEntity;
import jakarta.mail.MessagingException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseBodyEntity<String> handleGlobalException(Exception e) {
        String failMsg = e.getMessage();
        return ResponseBodyEntity.fail(failMsg);
    }


    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseBodyEntity<?> exceptionHandler(BindException e) {
        String failMsg = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseBodyEntity.fail(failMsg);
    }





}
