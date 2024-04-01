package com.yapikredi.Permission.Service.convertor;

import com.yapikredi.Permission.Service.model.request.EmployeeCreateRequest;
import com.yapikredi.Permission.Service.model.response.EmployeeResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeConvertorTest {

    @Test
    public void fromCreateRequestToEntity_ShouldConvertEmployeeCreateRequestToEmployeeEntity() {
        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setFirstName("Ahmet");
        request.setLastName("Yakup");
        request.setIdentityNumber("234");

        EmployeeEntity result = EmployeeConvertor.fromCreateRequestToEntity(request);

        assertEquals("Ahmet", result.getFirstName());
        assertEquals("Yakup", result.getLastName());
        assertEquals("234", result.getIdentityNumber());
        assertEquals(BigDecimal.valueOf(5), result.getTotalPermission());
    }

    @Test
    public void fromEntityToResponse_ShouldConvertEmployeeEntityToEmployeeResponse() {
        LocalDateTime localDateTime = LocalDateTime.now();

        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(1L);
        entity.setFirstName("Ahmet");
        entity.setLastName("Yakup");
        entity.setHireDate(localDateTime);
        entity.setIdentityNumber("234");
        entity.setTotalPermission(BigDecimal.TEN);

        EmployeeResponse result = EmployeeConvertor.fromEntityToResponse(entity);

        assertEquals(1L, result.getId());
        assertEquals("Ahmet", result.getFirstName());
        assertEquals("Yakup", result.getLastName());
        assertEquals(localDateTime, result.getHireDate());
        assertEquals("234", result.getIdentityNumber());
        assertEquals(BigDecimal.TEN, result.getTotalPermission());
    }
}
