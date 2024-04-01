package com.yapikredi.Permission.Service.web.controller;

import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionConfirmationRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.service.PermissionApprovalService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PermissionApprovalController.class)
public class PermissionApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PermissionApprovalService permissionApprovalService;

    @Test
    void getPermissionByManagerId_ShouldReturnPermissionResponses_WhenManagerIdIsValid() throws Exception {
        Long managerId = 1L;
        Map<Long, List<PermissionResponse>> permissionsMap = new HashMap<>();
        when(permissionApprovalService.getPermissionByManagerId(managerId)).thenReturn(permissionsMap);

        mockMvc.perform(get("/approval/{manager_id}", managerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void getPendingPermissionByManagerId_ShouldReturnPermissionResponses_WhenManagerIdAndStatusAreValid() throws Exception {
        Long managerId = 1L;
        PermissionStatus status = PermissionStatus.PENDING_APPROVAL;
        Map<Long, List<PermissionResponse>> permissionsMap = new HashMap<>();
        when(permissionApprovalService.getPendingPermissionByManagerId(managerId, status)).thenReturn(permissionsMap);

        mockMvc.perform(get("/approval/filter/{manager_id}", managerId)
                        .param("status", status.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void permissionConfirmation_ShouldReturnPermissionResponse_WhenRequestIsValid() throws Exception {
        PermissionConfirmationRequest request = new PermissionConfirmationRequest();
        PermissionResponse expectedResponse = new PermissionResponse();
        when(permissionApprovalService.permissionConfirmation(any(PermissionConfirmationRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/approval/confirmation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
