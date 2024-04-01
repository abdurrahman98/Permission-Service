package com.yapikredi.Permission.Service.service;


import com.yapikredi.Permission.Service.convertor.PermissionConvertor;
import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.ConfirmationStatus;
import com.yapikredi.Permission.Service.model.enums.ErrorCodeEnum;
import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionConfirmationRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;
import com.yapikredi.Permission.Service.persistence.repository.EmployeeRepository;
import com.yapikredi.Permission.Service.persistence.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionApprovalService {

    private final PermissionRepository permissionRepository;
    private final EmployeeRepository employeeRepository;


    public Map<Long, List<PermissionResponse>> getPermissionByManagerId(Long managerId) {
        List<EmployeeEntity> employeeEntities = employeeRepository.findByManagerId(managerId);

        return employeeEntities.stream().collect(Collectors.toMap(
                EmployeeEntity::getId,
                e -> permissionRepository.findByEmployeeEntity_Id(e.getId()).stream().map(PermissionConvertor::fromEntityToResponse).collect(Collectors.toList()))
        );
    }

    @Transactional
    public PermissionResponse permissionConfirmation(PermissionConfirmationRequest permissionConfirmationRequest) {
        EmployeeEntity employeeEntity = employeeRepository.findByIdLock(permissionConfirmationRequest.getEmployeeId())
                .orElseThrow(() -> new PermissionServiceException(ErrorCodeEnum.EMPLOYEE_NOT_FOUND));


        if (employeeEntity.getManager() == null || !employeeEntity.getManager().getId().equals(permissionConfirmationRequest.getManagerId())) {
            throw new RuntimeException();
        }

        PermissionEntity permissionEntity = permissionRepository.findById(permissionConfirmationRequest.getPermissionId())
                .orElseThrow(() -> new PermissionServiceException(ErrorCodeEnum.PERMISSION_NOT_FOUND));

        if (!permissionEntity.getEmployeeEntity().getId().equals(permissionConfirmationRequest.getEmployeeId())) {
            throw new RuntimeException();
        }

        if (!permissionEntity.getStatus().equals(PermissionStatus.PENDING_APPROVAL)) {
            throw new RuntimeException();
        }

        if (permissionConfirmationRequest.getConfirmationStatus().equals(ConfirmationStatus.APPROVED)) {
            permissionEntity.setStatus(PermissionStatus.APPROVED);
            permissionEntity = permissionRepository.save(permissionEntity);
        } else if (permissionConfirmationRequest.getConfirmationStatus().equals(ConfirmationStatus.REJECTED)) {
            employeeEntity.setTotalPermission(employeeEntity.getTotalPermission().add(permissionEntity.getAmount()));
            employeeRepository.save(employeeEntity);
            permissionEntity.setStatus(PermissionStatus.REJECTED);
            permissionEntity = permissionRepository.save(permissionEntity);
        } else {
            throw new PermissionServiceException(ErrorCodeEnum.UNKNOWN_CONFIRMATION_STATUS);
        }

        return PermissionConvertor.fromEntityToResponse(permissionEntity);
    }

    public Map<Long, List<PermissionResponse>> getPendingPermissionByManagerId(Long managerId, PermissionStatus permissionStatus) {
        List<EmployeeEntity> employeeEntities = employeeRepository.findByManagerId(managerId);

        return employeeEntities.stream().collect(Collectors.toMap(
                EmployeeEntity::getId,
                e -> permissionRepository.findByEmployeeEntity_IdAndStatus(e.getId(), permissionStatus).stream().map(PermissionConvertor::fromEntityToResponse).collect(Collectors.toList()))
        );

    }
}
