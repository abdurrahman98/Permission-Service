package com.yapikredi.Permission.Service.service;

import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.ConfirmationStatus;
import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionConfirmationRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;
import com.yapikredi.Permission.Service.persistence.repository.EmployeeRepository;
import com.yapikredi.Permission.Service.persistence.repository.PermissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PermissionApprovalServiceTest {

    @InjectMocks
    PermissionApprovalService permissionApprovalService;

    @Mock
    PermissionRepository permissionRepository;

    @Mock
    EmployeeRepository employeeRepository;


    private Long managerId;
    private Long employeeId;
    private Long permissionId;
    private BigDecimal permissionAmount;
    private PermissionEntity permissionEntity;
    private EmployeeEntity employeeEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        managerId = 1L;
        employeeId = 2L;
        permissionId = 3L;
        permissionAmount = BigDecimal.TEN;

        employeeEntity = new EmployeeEntity();
        employeeEntity.setFirstName("Ahmet");
        employeeEntity.setLastName("Yakup");
        employeeEntity.setId(employeeId);
        employeeEntity.setManager(EmployeeEntity.builder().id(managerId).build());
        employeeEntity.setTotalPermission(BigDecimal.ZERO);

        permissionEntity = new PermissionEntity();
        permissionEntity.setId(permissionId);
        permissionEntity.setEmployeeEntity(employeeEntity);
        permissionEntity.setStatus(PermissionStatus.PENDING_APPROVAL);
        permissionEntity.setAmount(permissionAmount);
    }

    @Test
    public void getPermissionByManagerId_ShouldReturnPermissionMap() {
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(employeeEntity);
        when(employeeRepository.findByManagerId(managerId)).thenReturn(employeeEntities);

        List<PermissionEntity> permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);
        when(permissionRepository.findByEmployeeEntity_Id(any())).thenReturn(permissionEntities);

        Map<Long, List<PermissionResponse>> result = permissionApprovalService.getPermissionByManagerId(managerId);

        assertNotNull(result);
        assertTrue(result.containsKey(employeeId));
        assertEquals(1, result.get(employeeId).size());
        assertEquals(permissionAmount, result.get(employeeId).get(0).getAmount());
    }

    @Test
    public void permissionConfirmation_ShouldApprovePermission_WhenConfirmationStatusIsApproved() {
        PermissionConfirmationRequest request = new PermissionConfirmationRequest();
        request.setManagerId(managerId);
        request.setEmployeeId(employeeId);
        request.setPermissionId(permissionId);
        request.setConfirmationStatus(ConfirmationStatus.APPROVED);

        when(employeeRepository.findByIdLock(employeeId)).thenReturn(java.util.Optional.of(employeeEntity));
        when(permissionRepository.findById(permissionId)).thenReturn(java.util.Optional.of(permissionEntity));
        when(permissionRepository.save(permissionEntity)).thenReturn(permissionEntity);

        PermissionResponse result = permissionApprovalService.permissionConfirmation(request);

        assertNotNull(result);
        assertEquals(PermissionStatus.APPROVED.name(), result.getStatus());
        verify(permissionRepository, times(1)).save(any());
    }

    @Test
    public void permissionConfirmation_ShouldRejectPermission_WhenConfirmationStatusIsRejected() {
        PermissionConfirmationRequest request = new PermissionConfirmationRequest();
        request.setManagerId(managerId);
        request.setEmployeeId(employeeId);
        request.setPermissionId(permissionId);
        request.setConfirmationStatus(ConfirmationStatus.REJECTED);

        when(employeeRepository.findByIdLock(employeeId)).thenReturn(java.util.Optional.of(employeeEntity));
        when(permissionRepository.findById(permissionId)).thenReturn(java.util.Optional.of(permissionEntity));
        when(permissionRepository.save(permissionEntity)).thenReturn(permissionEntity);

        PermissionResponse result = permissionApprovalService.permissionConfirmation(request);

        assertNotNull(result);
        assertEquals(PermissionStatus.REJECTED.name(), result.getStatus());
        verify(employeeRepository, times(1)).save(any());
        verify(permissionRepository, times(1)).save(any());
    }

    @Test
    public void permissionConfirmation_ShouldThrowException_WhenEmployeeNotFound() {
        PermissionConfirmationRequest request = new PermissionConfirmationRequest();
        request.setEmployeeId(employeeId);
        request.setPermissionId(permissionId);

        when(employeeRepository.findByIdLock(employeeId)).thenReturn(java.util.Optional.empty());

        assertThrows(PermissionServiceException.class, () -> permissionApprovalService.permissionConfirmation(request));
        verify(permissionRepository, never()).findById(any());
    }


    @Test
    public void getPendingPermissionByManagerId_ShouldReturnPendingPermissionMap() {
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(employeeEntity);
        when(employeeRepository.findByManagerId(managerId)).thenReturn(employeeEntities);

        List<PermissionEntity> permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);
        when(permissionRepository.findByEmployeeEntity_IdAndStatus(employeeId, PermissionStatus.PENDING_APPROVAL)).thenReturn(permissionEntities);

        Map<Long, List<PermissionResponse>> result = permissionApprovalService.getPendingPermissionByManagerId(managerId, PermissionStatus.PENDING_APPROVAL);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(employeeId).size());
        assertEquals(permissionEntity.getAmount(), result.get(employeeId).get(0).getAmount());
        assertEquals(permissionEntity.getStatus().name(), result.get(employeeId).get(0).getStatus());
    }
}
