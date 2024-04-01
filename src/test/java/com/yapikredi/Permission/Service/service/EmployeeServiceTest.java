package com.yapikredi.Permission.Service.service;

import com.yapikredi.Permission.Service.exception.PermissionServiceException;
import com.yapikredi.Permission.Service.model.enums.ErrorCodeEnum;
import com.yapikredi.Permission.Service.model.request.EmployeeCreateRequest;
import com.yapikredi.Permission.Service.model.request.ManagerAssignmentRequest;
import com.yapikredi.Permission.Service.model.response.EmployeeResponse;
import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import com.yapikredi.Permission.Service.persistence.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldThrowExceptionWhenEmployeeNotFoundInDatabase() {
        Long validId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();

        EmployeeEntity mockEmployee = new EmployeeEntity();
        mockEmployee.setId(validId);
        mockEmployee.setFirstName("Ahmet");
        mockEmployee.setLastName("Yakup");
        mockEmployee.setHireDate(localDateTime);
        mockEmployee.setIdentityNumber("234");
        mockEmployee.setTotalPermission(BigDecimal.valueOf(5));

        when(employeeRepository.findById(validId)).thenReturn(Optional.of(mockEmployee));


        EmployeeResponse result = employeeService.getEmployeeById(validId);


        assertNotNull(result);
        assertEquals("Ahmet", result.getFirstName());
        assertEquals("Yakup", result.getLastName());
        assertEquals("234", result.getIdentityNumber());
        assertEquals(localDateTime, result.getHireDate());
        assertEquals(BigDecimal.valueOf(5), result.getTotalPermission());
    }
    @Test
    public void shouldReturnEmployeeDetailsWhenValidIdGiven() {

        Long invalidId = -1L;
        when(employeeRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(PermissionServiceException.class, () -> employeeService.getEmployeeById(invalidId));
    }

    @Test
    public void shouldCreateEmployeeSuccessfully() {
        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setFirstName("Ahmet");
        request.setLastName("Yakup");
        request.setIdentityNumber("234");

        EmployeeEntity mockSavedEmployee = new EmployeeEntity();
        mockSavedEmployee.setId(1L);
        mockSavedEmployee.setFirstName("Ahmet");
        mockSavedEmployee.setLastName("Yakup");
        mockSavedEmployee.setIdentityNumber("234");
        mockSavedEmployee.setTotalPermission(BigDecimal.ZERO);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(mockSavedEmployee);

        EmployeeResponse result = employeeService.createEmployee(request);

        assertNotNull(result);
        assertEquals("Ahmet", result.getFirstName());
        assertEquals("Yakup", result.getLastName());
        assertEquals("234", result.getIdentityNumber());
        assertEquals(BigDecimal.ZERO, result.getTotalPermission());
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    public void shouldReturnAllEmployeesSuccessfully() {
        EmployeeEntity employee1 = new EmployeeEntity();
        employee1.setId(1L);
        employee1.setFirstName("Ahmet");
        employee1.setLastName("Yakup");
        employee1.setIdentityNumber("123");
        employee1.setTotalPermission(BigDecimal.ZERO);

        EmployeeEntity employee2 = new EmployeeEntity();
        employee2.setId(2L);
        employee2.setFirstName("Ayse");
        employee2.setLastName("Kara");
        employee2.setIdentityNumber("234");
        employee2.setTotalPermission(BigDecimal.ONE);

        List<EmployeeEntity> mockEmployees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        List<EmployeeResponse> result = employeeService.getAllEmployee();

        assertNotNull(result);

        assertEquals(2, result.size());
        assertEquals("Ahmet", result.get(0).getFirstName());
        assertEquals("Yakup", result.get(0).getLastName());
        assertEquals("123", result.get(0).getIdentityNumber());
        assertEquals(BigDecimal.ZERO, result.get(0).getTotalPermission());

        assertEquals("Ayse", result.get(1).getFirstName());
        assertEquals("Kara", result.get(1).getLastName());
        assertEquals("234", result.get(1).getIdentityNumber());
        assertEquals(BigDecimal.ONE, result.get(1).getTotalPermission());

    }

    @Test
    public void shouldAssignManagerSuccessfully() {
        Long employeeId = 1L;
        Long managerId = 2L;

        EmployeeEntity mockEmployee = new EmployeeEntity();
        mockEmployee.setFirstName("Ahmet");
        mockEmployee.setLastName("Yakup");
        mockEmployee.setId(employeeId);

        EmployeeEntity mockManager = new EmployeeEntity();
        mockManager.setId(managerId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.findById(managerId)).thenReturn(Optional.of(mockManager));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(mockEmployee);

        ManagerAssignmentRequest request = new ManagerAssignmentRequest();
        request.setEmployeeId(employeeId);
        request.setManagerId(managerId);

        EmployeeResponse result = employeeService.managerAssigment(request);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());
        assertEquals("Ahmet", result.getFirstName());
        assertEquals("Yakup", result.getLastName());

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).findById(managerId);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    public void shouldThrowExceptionWhenEmployeeNotFound() {
        ManagerAssignmentRequest request = new ManagerAssignmentRequest();
        request.setEmployeeId(1L);
        request.setManagerId(2L);
        when(employeeRepository.findById(request.getEmployeeId())).thenReturn(Optional.empty());

        assertThrows(PermissionServiceException.class, () -> {
            employeeService.managerAssigment(request);
        });

        verify(employeeRepository, times(1)).findById(request.getEmployeeId());
        verify(employeeRepository, never()).findById(request.getManagerId());
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    public void shouldThrowExceptionWhenManagerNotFound() {
        ManagerAssignmentRequest request = new ManagerAssignmentRequest();
        request.setEmployeeId(1L);
        request.setManagerId(2L);

        when(employeeRepository.findById(request.getEmployeeId())).thenReturn(Optional.of(new EmployeeEntity()));
        when(employeeRepository.findById(request.getManagerId())).thenReturn(Optional.empty());

        assertThrows(PermissionServiceException.class, () -> {
            employeeService.managerAssigment(request);
        });

        verify(employeeRepository, times(1)).findById(request.getEmployeeId());
        verify(employeeRepository, times(1)).findById(request.getManagerId());
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    public void shouldAddYearlyPermissionSuccessfully() {
        Long employeeId = 1L;
        BigDecimal initialPermission = BigDecimal.TEN;
        BigDecimal permissionToAdd = new BigDecimal("5");

        EmployeeEntity mockEmployee = new EmployeeEntity();
        mockEmployee.setId(employeeId);
        mockEmployee.setTotalPermission(initialPermission);

        when(employeeRepository.findByIdLock(employeeId)).thenReturn(Optional.of(mockEmployee));

        employeeService.addYearlyPermission(employeeId, permissionToAdd);

        assertEquals(initialPermission.add(permissionToAdd), mockEmployee.getTotalPermission());
        verify(employeeRepository, times(1)).findByIdLock(employeeId);
        verify(employeeRepository, times(1)).save(mockEmployee);
    }

    @Test
    public void shouldNotAddPermissionIfEmployeeNotFound() {
        Long employeeId = 1L;
        BigDecimal permissionToAdd = new BigDecimal("5");

        when(employeeRepository.findByIdLock(employeeId)).thenReturn(Optional.empty());

        employeeService.addYearlyPermission(employeeId, permissionToAdd);

        verify(employeeRepository, times(1)).findByIdLock(employeeId);
        verify(employeeRepository, never()).save(any(EmployeeEntity.class));
    }

    @Test
    public void shouldReturnEmployeesByHireDate() {
        Integer day = 1;
        Integer month = 4;
        EmployeeEntity employee1 = new EmployeeEntity();
        employee1.setId(1L);
        employee1.setFirstName("Ahmet");
        employee1.setLastName("Yakup");
        employee1.setHireDate(LocalDateTime.of(2022, 4, 1, 0, 0));
        employee1.setTotalPermission(BigDecimal.TEN);
        employee1.setIdentityNumber("123");

        EmployeeEntity employee2 = new EmployeeEntity();
        employee2.setId(2L);
        employee2.setFirstName("Ayse");
        employee2.setLastName("Kara");
        employee2.setHireDate(LocalDateTime.of(2022, 4, 1, 0, 0));
        employee2.setTotalPermission(BigDecimal.ZERO);
        employee2.setIdentityNumber("234");

        List<EmployeeEntity> mockEmployees = Arrays.asList(employee1, employee2);
        when(employeeRepository.findEmployeesByHireDateDayAndMonth(day, month)).thenReturn(mockEmployees);

        List<EmployeeEntity> result = employeeService.getEmployeeByHireDate(day, month);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(employee1.getId(), result.get(0).getId());
        assertEquals(employee1.getFirstName(), result.get(0).getFirstName());
        assertEquals(employee1.getLastName(), result.get(0).getLastName());
        assertEquals(employee1.getHireDate(), result.get(0).getHireDate());
        assertEquals(employee1.getTotalPermission(), result.get(0).getTotalPermission());
        assertEquals(employee1.getIdentityNumber(), result.get(0).getIdentityNumber());

        assertEquals(employee2.getId(), result.get(1).getId());
        assertEquals(employee2.getFirstName(), result.get(1).getFirstName());
        assertEquals(employee2.getLastName(), result.get(1).getLastName());
        assertEquals(employee2.getHireDate(), result.get(1).getHireDate());
        assertEquals(employee2.getTotalPermission(), result.get(1).getTotalPermission());
        assertEquals(employee2.getIdentityNumber(), result.get(1).getIdentityNumber());
    }
}

