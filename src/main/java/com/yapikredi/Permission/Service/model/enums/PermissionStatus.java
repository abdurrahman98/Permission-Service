package com.yapikredi.Permission.Service.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionStatus {
    PENDING_APPROVAL,
    APPROVED,
    REJECTED
}
