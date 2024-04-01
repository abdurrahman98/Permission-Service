package com.yapikredi.Permission.Service.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapikredi.Permission.Service.model.request.PermissionCreateRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermissionController.class)
public class PermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermissionService permissionService;

    @Test
    void getAllPermission_ShouldReturnPermissionResponses() throws Exception {
        Map<Long, List<PermissionResponse>> permissionsMap = new HashMap<>();
        when(permissionService.getAll()).thenReturn(permissionsMap);

        mockMvc.perform(get("/permission/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void getByEmployeeId_ShouldReturnPermissionResponses_WhenEmployeeIdIsValid() throws Exception {
        Long employeeId = 1L;
        List<PermissionResponse> permissionsList = Collections.emptyList();
        when(permissionService.getByEmployeeId(employeeId)).thenReturn(permissionsList);

        mockMvc.perform(get("/permission/{employee_id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createPermission_ShouldReturnPermissionResponse_WhenRequestIsValid() throws Exception {
        PermissionCreateRequest request = new PermissionCreateRequest();
        PermissionResponse expectedResponse = new PermissionResponse();
        when(permissionService.createPermission(any(PermissionCreateRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/permission/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
