package com.yapikredi.Permission.Service.service;

import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionCreateRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;
import com.yapikredi.Permission.Service.persistence.repository.EmployeeRepository;
import com.yapikredi.Permission.Service.persistence.repository.PermissionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PermissionServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    PermissionRepository permissionRepository;
    @InjectMocks
    PermissionService permissionService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPermission_ShouldCreatePermissionSuccessfully() {
        PermissionCreateRequest request = new PermissionCreateRequest();
        request.setEmployeeId(1L);
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusDays(1));

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setTotalPermission(BigDecimal.TEN);

        when(employeeRepository.findByIdLock(request.getEmployeeId())).thenReturn(Optional.of(employeeEntity));
        when(permissionRepository.save(any(PermissionEntity.class))).thenAnswer(invocation -> {
            PermissionEntity permissionEntity = invocation.getArgument(0);
            permissionEntity.setId(1L);
            return permissionEntity;
        });

        PermissionResponse result = permissionService.createPermission(request);

        assertNotNull(result);
        assertEquals(PermissionStatus.PENDING_APPROVAL.name(), result.getStatus());
        verify(employeeRepository, times(1)).findByIdLock(request.getEmployeeId());
        verify(permissionRepository, times(1)).save(any(PermissionEntity.class));
    }

    @Test
    public void createPermission_ShouldThrowException_WhenEmployeeNotFound() {
        PermissionCreateRequest request = new PermissionCreateRequest();
        request.setEmployeeId(1L);

        when(employeeRepository.findByIdLock(request.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(PermissionServiceException.class, () -> permissionService.createPermission(request));
        verify(employeeRepository, times(1)).findByIdLock(request.getEmployeeId());
        verify(permissionRepository, never()).save(any());
    }


    @Test
    public void getAll_ShouldReturnAllPermissionsMappedByEmployeeId() {
        LocalDateTime localDateTime = LocalDateTime.now();
        PermissionEntity permissionEntity = PermissionEntity.builder()
                .id(1L)
                .status(PermissionStatus.PENDING_APPROVAL)
                .startDate(localDateTime)
                .endDate(localDateTime.plusDays(2))
                .amount(BigDecimal.TEN)
                .build();

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setPermissionEntities(Collections.singletonList(permissionEntity));

        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employeeEntity));

        Map<Long, List<PermissionResponse>> result = permissionService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.containsKey(1L));
        assertEquals(1, result.get(1L).size());
        assertEquals(permissionEntity.getStatus().name(), result.get(1L).get(0).getStatus());
        assertEquals(permissionEntity.getAmount(), result.get(1L).get(0).getAmount());
        assertEquals(permissionEntity.getStartDate(), result.get(1L).get(0).getStartDate());
        assertEquals(permissionEntity.getEndDate(), result.get(1L).get(0).getEndDate());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void getByEmployeeId_ShouldReturnPermissionsByEmployeeId() {
        Long employeeId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        PermissionEntity permissionEntity = PermissionEntity.builder()
                .id(2L)
                .status(PermissionStatus.PENDING_APPROVAL)
                .startDate(localDateTime)
                .endDate(localDateTime.plusDays(2))
                .amount(BigDecimal.TEN)
                .build();

        when(permissionRepository.findByEmployeeEntity_Id(employeeId)).thenReturn(Collections.singletonList(permissionEntity));

        List<PermissionResponse> result = permissionService.getByEmployeeId(employeeId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(permissionEntity.getStatus().name(), result.get(0).getStatus());
        assertEquals(permissionEntity.getAmount(), result.get(0).getAmount());
        assertEquals(permissionEntity.getStartDate(), result.get(0).getStartDate());
        assertEquals(permissionEntity.getEndDate(), result.get(0).getEndDate());
        verify(permissionRepository, times(1)).findByEmployeeEntity_Id(employeeId);
    }
}

