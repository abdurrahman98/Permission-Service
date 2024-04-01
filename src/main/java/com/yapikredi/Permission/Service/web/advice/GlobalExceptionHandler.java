package com.yapikredi.Permission.Service.web.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.ErrorCodeEnum;
import com.yapikredi.Permission.Service.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DOMAIN = "permission-service";
    private final MessageSource messageSource;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ExceptionHandler(PermissionServiceException.class)
    public ResponseEntity<Object> handle(PermissionServiceException exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        ErrorCodeEnum errorCodeEnum = exception.getErrorCodeEnum();

        errorResponse.setDomain(DOMAIN);
        errorResponse.setCode(errorCodeEnum.getCode());
        errorResponse.setMessage(messageSource.getMessage(errorCodeEnum.getKey(), null, errorCodeEnum.getMessage(), LocaleContextHolder.getLocale()));
        errorResponse.setExtras(exception.getErrorMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> extras = new ArrayList<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            extras.add(error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setDomain(DOMAIN);
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setExtras(extras);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setDomain(DOMAIN);
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setExtras(exception.getCause().getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}