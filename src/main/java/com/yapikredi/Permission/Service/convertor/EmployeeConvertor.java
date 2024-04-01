package com.yapikredi.Permission.Service.convertor;


import com.yapikredi.Permission.Service.model.request.EmployeeCreateRequest;
import com.yapikredi.Permission.Service.model.response.EmployeeResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EmployeeConvertor {

    public static EmployeeEntity fromCreateRequestToEntity(EmployeeCreateRequest employeeCreateRequest) {
        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setFirstName(employeeCreateRequest.getFirstName());
        employeeEntity.setLastName(employeeCreateRequest.getLastName());
        employeeEntity.setIdentityNumber(employeeCreateRequest.getIdentityNumber());
        employeeEntity.setHireDate(LocalDateTime.now());
        employeeEntity.setTotalPermission(BigDecimal.valueOf(5));

        return employeeEntity;
    }

    public static EmployeeResponse fromEntityToResponse(EmployeeEntity employeeEntity) {
        EmployeeResponse employeeResponse = new EmployeeResponse();

        employeeResponse.setId(employeeEntity.getId());
        employeeResponse.setFirstName(employeeEntity.getFirstName());
        employeeResponse.setLastName(employeeEntity.getLastName());
        employeeResponse.setHireDate(employeeEntity.getHireDate());
        employeeResponse.setIdentityNumber(employeeEntity.getIdentityNumber());
        employeeResponse.setTotalPermission(employeeEntity.getTotalPermission());

        return employeeResponse;
    }
}
