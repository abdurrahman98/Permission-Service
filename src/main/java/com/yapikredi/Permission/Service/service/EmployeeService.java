package com.yapikredi.Permission.Service.service;

import com.yapikredi.Permission.Service.convertor.EmployeeConvertor;
import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.ErrorCodeEnum;
import com.yapikredi.Permission.Service.model.request.EmployeeCreateRequest;
import com.yapikredi.Permission.Service.model.request.ManagerAssignmentRequest;
import com.yapikredi.Permission.Service.model.response.EmployeeResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeResponse getEmployeeById(Long id) {
        return EmployeeConvertor.fromEntityToResponse(employeeRepository.findById(id)
                .orElseThrow(() -> new PermissionServiceException(ErrorCodeEnum.EMPLOYEE_NOT_FOUND)));
    }

    public EmployeeResponse createEmployee(EmployeeCreateRequest employeeCreateRequest) {
        EmployeeEntity employeeEntity = employeeRepository.save(EmployeeConvertor.fromCreateRequestToEntity(employeeCreateRequest));

        return EmployeeConvertor.fromEntityToResponse(employeeEntity);
    }

    public List<EmployeeResponse> getAllEmployee() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeConvertor::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public EmployeeResponse managerAssigment(ManagerAssignmentRequest managerAssignmentRequest) {
        EmployeeEntity employeeEntity = employeeRepository.findById(managerAssignmentRequest.getEmployeeId())
                .orElseThrow(() -> new PermissionServiceException(ErrorCodeEnum.EMPLOYEE_NOT_FOUND));
        EmployeeEntity managerEntity = employeeRepository.findById(managerAssignmentRequest.getManagerId())
                .orElseThrow(() -> new PermissionServiceException(ErrorCodeEnum.MANAGER_NOT_FOUND));

        employeeEntity.setManager(managerEntity);

        employeeEntity = employeeRepository.save(employeeEntity);

        return EmployeeConvertor.fromEntityToResponse(employeeEntity);
    }

    @Transactional
    public void addYearlyPermission(Long id, BigDecimal amount) {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByIdLock(id);

        if (optionalEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity = optionalEmployeeEntity.get();

            employeeEntity.setTotalPermission(employeeEntity.getTotalPermission().add(amount));

            employeeRepository.save(employeeEntity);
        }
    }

    public List<EmployeeEntity> getEmployeeByHireDate(Integer day, Integer month) {
        return employeeRepository.findEmployeesByHireDateDayAndMonth(day, month);
    }
}
