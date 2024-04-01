package com.yapikredi.Permission.Service.convertor;


import com.yapikredi.Permission.Service.model.request.PermissionCreateRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;

import java.math.BigDecimal;

public class PermissionConvertor {


    public static PermissionResponse fromEntityToResponse(PermissionEntity permissionEntity) {
        return PermissionResponse.builder()
                .amount(permissionEntity.getAmount())
                .startDate(permissionEntity.getStartDate())
                .endDate(permissionEntity.getEndDate())
                .status(permissionEntity.getStatus().name())
                .build();
    }

    public static PermissionEntity fromCreateToEntity(PermissionCreateRequest permissionCreateRequest, EmployeeEntity employeeEntity, BigDecimal permissionAmount) {
        PermissionEntity permissionEntity = new PermissionEntity();

        permissionEntity.setEmployeeEntity(employeeEntity);
        permissionEntity.setStartDate(permissionCreateRequest.getStartDate());
        permissionEntity.setEndDate(permissionCreateRequest.getEndDate());
        permissionEntity.setAmount(permissionAmount);

        return permissionEntity;
    }
}
