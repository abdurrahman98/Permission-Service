package com.yapikredi.Permission.Service.service;


import com.yapikredi.Permission.Service.convertor.PermissionConvertor;
import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.ErrorCodeEnum;
import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionCreateRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;
import com.yapikredi.Permission.Service.persistence.repository.EmployeeRepository;
import com.yapikredi.Permission.Service.persistence.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final EmployeeRepository employeeRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public PermissionResponse createPermission(PermissionCreateRequest permissionCreateRequest) {
        EmployeeEntity employeeEntity = employeeRepository.findByIdLock(permissionCreateRequest.getEmployeeId())
                .orElseThrow(() -> new PermissionServiceException(ErrorCodeEnum.EMPLOYEE_NOT_FOUND));

        dateValidation(permissionCreateRequest);

        BigDecimal permissionAmount = calculatePermissionDays(permissionCreateRequest.getStartDate(), permissionCreateRequest.getEndDate());

        if (employeeEntity.getTotalPermission().compareTo(permissionAmount) < 0) {
            throw new RuntimeException();
        }

        employeeEntity.setTotalPermission(employeeEntity.getTotalPermission().subtract(permissionAmount));

        PermissionEntity permissionEntity = PermissionConvertor.fromCreateToEntity(permissionCreateRequest, employeeEntity, permissionAmount);
        permissionEntity.setStatus(PermissionStatus.PENDING_APPROVAL);

        PermissionEntity resultPermission = permissionRepository.save(permissionEntity);

        employeeRepository.save(employeeEntity);

        return PermissionConvertor.fromEntityToResponse(resultPermission);
    }

    private void dateValidation(PermissionCreateRequest permissionCreateRequest) {
        if (permissionCreateRequest.getStartDate().isAfter(permissionCreateRequest.getEndDate())){
            throw new PermissionServiceException(ErrorCodeEnum.WRONG_DATE_PARAMETER);
        }

    }

    private BigDecimal calculatePermissionDays(LocalDateTime startDate, LocalDateTime endDate) {

        BigDecimal workingDays = BigDecimal.ZERO;

        for (LocalDate date = startDate.toLocalDate(); !date.isAfter(endDate.toLocalDate()); date = date.plusDays(1)) {
            if (date.getDayOfWeek().getValue() < 6) {
                if (date.isEqual(startDate.toLocalDate()) && startDate.toLocalTime().isBefore(LocalTime.NOON)) {
                    workingDays = workingDays.add(new BigDecimal("0.5"));
                } else if (date.isEqual(endDate.toLocalDate()) && endDate.toLocalTime().isBefore(LocalTime.NOON)) {
                    workingDays = workingDays.add(new BigDecimal("0.5"));
                } else if (date.isEqual(startDate.toLocalDate()) && startDate.toLocalTime().isAfter(LocalTime.NOON) && startDate.toLocalTime().isBefore(LocalTime.of(18, 0))) {
                    workingDays = workingDays.add(new BigDecimal("0.5"));
                } else if (date.isEqual(endDate.toLocalDate()) && endDate.toLocalTime().isAfter(LocalTime.NOON) && endDate.toLocalTime().isBefore(LocalTime.of(18, 0))) {
                    workingDays = workingDays.add(new BigDecimal("0.5"));
                } else {
                    workingDays = workingDays.add(BigDecimal.ONE);
                }
            }
        }
        return workingDays;
    }

    public Map<Long, List<PermissionResponse>> getAll() {
        return employeeRepository.findAll().stream()
                .collect(Collectors.toMap(
                        EmployeeEntity::getId,
                        e -> e.getPermissionEntities().stream()
                                .map(PermissionConvertor::fromEntityToResponse)
                                .collect(Collectors.toList()))
                );
    }


    public List<PermissionResponse> getByEmployeeId(Long employeeId) {
        List<PermissionEntity> permissionEntity = permissionRepository.findByEmployeeEntity_Id(employeeId);


        return permissionEntity.stream().map(PermissionConvertor::fromEntityToResponse).collect(Collectors.toList());
    }
}
