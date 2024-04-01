package com.yapikredi.Permission.Service.service;


import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.entity.PermissionTypeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionSchedulerService {

    private final EmployeeService employeeService;
    private final PermissionTypeService permissionTypeService;

    public void process(LocalDateTime now) {
        List<EmployeeEntity> employeeEntities = employeeService.getEmployeeByHireDate(now.toLocalDate().getDayOfMonth(), now.toLocalDate().getMonthValue());
        employeeEntities.forEach(employee -> {
            int employeeYears =  LocalDate.now().getYear() - employee.getHireDate().getYear();
            permissionTypeService.getAllPermissionType().stream()
                    .filter(e-> employeeYears > e.getStartYears() && employeeYears <= e.getEndYears())
                    .map(PermissionTypeEntity::getAmount)
                    .findFirst().ifPresent(e -> {
                        employeeService.addYearlyPermission(employee.getId(),e);
                    });
        });
    }
}
