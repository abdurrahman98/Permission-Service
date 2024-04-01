package com.yapikredi.Permission.Service.service;

import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionTypeEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class PermissionSchedulerServiceTest {
    @Mock
    EmployeeService employeeService;
    @Mock
    PermissionTypeService permissionTypeService;
    @InjectMocks
    PermissionSchedulerService permissionSchedulerService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void process_ShouldAddYearlyPermission_WhenEligibleEmployeeFound() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDate hireDate = LocalDate.of(2020, 1, 1);

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setHireDate(hireDate.atStartOfDay());

        when(employeeService.getEmployeeByHireDate(1, 1)).thenReturn(Collections.singletonList(employeeEntity));

        PermissionTypeEntity permissionTypeEntity = new PermissionTypeEntity();
        permissionTypeEntity.setStartYears(0L);
        permissionTypeEntity.setEndYears(5L);
        permissionTypeEntity.setAmount(BigDecimal.TEN);

        when(permissionTypeService.getAllPermissionType()).thenReturn(Collections.singletonList(permissionTypeEntity));

        permissionSchedulerService.process(now);

        verify(employeeService, times(1)).addYearlyPermission(employeeEntity.getId(), BigDecimal.TEN);
    }

    @Test
    public void process_ShouldNotAddYearlyPermission_WhenNoEligibleEmployeeFound() {
        LocalDateTime now = LocalDateTime.of(2024, 4, 1, 0, 0);
        when(employeeService.getEmployeeByHireDate(1, 4)).thenReturn(Collections.emptyList());

        permissionSchedulerService.process(now);

        verify(employeeService, never()).addYearlyPermission(anyLong(), any(BigDecimal.class));
    }
}
