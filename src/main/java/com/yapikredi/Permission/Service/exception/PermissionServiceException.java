package com.yapikredi.Permission.Service.exception;

import com.yapikredi.Permission.Service.model.response.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.yapikredi.Permission.Service.model.enums.ErrorCodeEnum;

@Getter
@RequiredArgsConstructor
public class PermissionServiceException extends RuntimeException {
    private final Object errorMessage;
    private final ErrorCodeEnum errorCodeEnum;

    public PermissionServiceException(ErrorCodeEnum errorCodeEnum) {
        super();
        this.errorCodeEnum = errorCodeEnum;
        this.errorMessage = errorCodeEnum.getMessage();
    }

    public PermissionServiceException(ErrorCodeEnum errorCode, Long id) {
        super();
        this.errorCodeEnum = errorCode;
        this.errorMessage = errorCode.getMessage() + " with id: " + id;
    }

    public PermissionServiceException(ErrorResponse errorResponse) {
        super();
        this.errorCodeEnum = ErrorCodeEnum.findByCode(errorResponse.getCode());
        this.errorMessage = errorResponse.getMessage();
    }
}