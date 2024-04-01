package com.yapikredi.Permission.Service.web.controller;


import com.yapikredi.Permission.Service.model.request.EmployeeCreateRequest;
import com.yapikredi.Permission.Service.model.request.ManagerAssignmentRequest;
import com.yapikredi.Permission.Service.model.response.EmployeeResponse;
import com.yapikredi.Permission.Service.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }
    @GetMapping("/all")
    public List<EmployeeResponse> getAllEmployee() {
        return employeeService.getAllEmployee();
    }


    @PostMapping("/create")
    public EmployeeResponse createEmployee(@RequestBody EmployeeCreateRequest employeeCreateRequest) {
        return employeeService.createEmployee(employeeCreateRequest);
    }

    @PostMapping("/manager-assignment")
    public EmployeeResponse managerAssigment(@RequestBody ManagerAssignmentRequest managerAssignmentRequest) {
        return employeeService.managerAssigment(managerAssignmentRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }


}
