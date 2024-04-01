package com.yapikredi.Permission.Service.convertor;

import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionCreateRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermissionConvertorTest {

    @Test
    public void fromEntityToResponse_ShouldConvertPermissionEntityToPermissionResponse() {
        PermissionEntity entity = new PermissionEntity();
        entity.setAmount(BigDecimal.TEN);
        entity.setStartDate(LocalDateTime.now());
        entity.setEndDate(LocalDateTime.now().plusDays(1));
        entity.setStatus(PermissionStatus.PENDING_APPROVAL);

        PermissionResponse result = PermissionConvertor.fromEntityToResponse(entity);

        assertEquals(BigDecimal.TEN, result.getAmount());
        assertEquals(entity.getStartDate(), result.getStartDate());
        assertEquals(entity.getEndDate(), result.getEndDate());
        assertEquals("PENDING_APPROVAL", result.getStatus());
    }

    @Test
    public void fromCreateToEntity_ShouldConvertPermissionCreateRequestToPermissionEntity() {
        PermissionCreateRequest request = new PermissionCreateRequest();
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusDays(1));

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setFirstName("Ahmet");
        employeeEntity.setLastName("Yakup");
        employeeEntity.setIdentityNumber("234");

        BigDecimal permissionAmount = BigDecimal.valueOf(2);

        PermissionEntity result = PermissionConvertor.fromCreateToEntity(request, employeeEntity, permissionAmount);

        assertEquals(request.getStartDate(), result.getStartDate());
        assertEquals(request.getEndDate(), result.getEndDate());
        assertEquals(employeeEntity, result.getEmployeeEntity());
        assertEquals(permissionAmount, result.getAmount());
        assertEquals(employeeEntity.getFirstName(), result.getEmployeeEntity().getFirstName());
        assertEquals(employeeEntity.getLastName(), result.getEmployeeEntity().getLastName());
        assertEquals(employeeEntity.getIdentityNumber(), result.getEmployeeEntity().getIdentityNumber());
    }


}
