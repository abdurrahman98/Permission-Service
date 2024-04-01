package com.yapikredi.Permission.Service.web.controller;

import com.yapikredi.Permission.Service.model.request.EmployeeCreateRequest;
import com.yapikredi.Permission.Service.model.request.ManagerAssignmentRequest;
import com.yapikredi.Permission.Service.model.response.EmployeeResponse;
import com.yapikredi.Permission.Service.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_ShouldReturnEmployeeResponse_WhenEmployeeExists() throws Exception {
        EmployeeResponse expectedResponse = new EmployeeResponse();
        expectedResponse.setId(1L);
        expectedResponse.setFirstName("Ahmet");
        expectedResponse.setLastName("Yakup");

        when(employeeService.getEmployeeById(1L)).thenReturn(expectedResponse);

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ahmet"))
                .andExpect(jsonPath("$.lastName").value("Yakup"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getAllEmployee_ShouldReturnListOfEmployeeResponses_WhenEmployeesExist() throws Exception {
        List<EmployeeResponse> expectedResponses = Arrays.asList(
                EmployeeResponse.builder().id(1L).firstName("Ahmet").lastName("Yakup").build(),
                EmployeeResponse.builder().id(2L).firstName("Ayse").lastName("Kara").build()
        );

        when(employeeService.getAllEmployee()).thenReturn(expectedResponses);

        mockMvc.perform(get("/employee/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Ahmet"))
                .andExpect(jsonPath("$[0].lastName").value("Yakup"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Ayse"))
                .andExpect(jsonPath("$[1].lastName").value("Kara"));

        verify(employeeService, times(1)).getAllEmployee();
    }

    @Test
    void createEmployee_ShouldReturnEmployeeResponse_WhenEmployeeIsCreated() throws Exception {
        EmployeeCreateRequest request = new EmployeeCreateRequest();
        request.setFirstName("Ahmet");
        request.setLastName("Yakup");
        request.setIdentityNumber("234");

        EmployeeResponse expectedResponse = new EmployeeResponse();
        expectedResponse.setId(1L);
        expectedResponse.setFirstName("Ayse");
        expectedResponse.setLastName("Kara");
        expectedResponse.setIdentityNumber("123");

        when(employeeService.createEmployee(any(EmployeeCreateRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"identityNumber\": \"1234567890\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ahmet"))
                .andExpect(jsonPath("$.lastName").value("Yakup"));

        verify(employeeService, times(1)).createEmployee(any(EmployeeCreateRequest.class));
    }

    @Test
    void managerAssigment_ShouldReturnEmployeeResponse_WhenManagerIsAssigned() throws Exception {
        ManagerAssignmentRequest request = new ManagerAssignmentRequest();
        request.setEmployeeId(1L);
        request.setManagerId(2L);

        EmployeeResponse expectedResponse = new EmployeeResponse();
        expectedResponse.setId(1L);

        when(employeeService.managerAssigment(any(ManagerAssignmentRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/employee/manager-assignment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"employeeId\": 1, \"managerId\": 2 }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(employeeService, times(1)).managerAssigment(any(ManagerAssignmentRequest.class));
    }

    @Test
    void deleteEmployee_ShouldReturnOkStatus_WhenEmployeeIsDeleted() throws Exception {
        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }


}