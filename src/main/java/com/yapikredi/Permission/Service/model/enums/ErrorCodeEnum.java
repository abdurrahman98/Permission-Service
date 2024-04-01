package com.yapikredi.Permission.Service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ErrorCodeEnum {
    INTERNAL_SERVER_ERROR(1000, "internal-server-error","Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
    EMPLOYEE_NOT_FOUND(1, "employee-not-found", "Employee not found", HttpStatus.BAD_REQUEST),
    MANAGER_NOT_FOUND(2, "manger-not-found", "Manager not found", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(3, "permission-not-found", "Permission not found", HttpStatus.BAD_REQUEST),
    UNKNOWN_CONFIRMATION_STATUS(4, "unknown-confirmation-status", "Unknown confirmation status", HttpStatus.BAD_REQUEST),
    WRONG_DATE_PARAMETER(5, "wrong-date-parameter", "Wrong date parameter", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String key;
    private final String message;
    private final HttpStatus httpStatus;

    public static ErrorCodeEnum findByCode(int code) {
        return Arrays.stream(ErrorCodeEnum.values())
                .filter(c -> c.getCode() == code)
                .findFirst().orElse(INTERNAL_SERVER_ERROR);
    }
}
