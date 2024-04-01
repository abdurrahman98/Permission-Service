package com.yapikredi.Permission.Service.web.controller;


import com.sun.org.glassfish.gmbal.ParameterNames;
import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.model.request.PermissionConfirmationRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.service.PermissionApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/approval")
@RequiredArgsConstructor
public class PermissionApprovalController {
    private final PermissionApprovalService permissionApprovalService;

    @GetMapping("/{manager_id}")
    public Map<Long, List<PermissionResponse>> getPermissionByManagerId(@PathVariable(name = "manager_id") Long managerId){
        return permissionApprovalService.getPermissionByManagerId(managerId);
    }

    @GetMapping("/filter/{manager_id}")
    public Map<Long, List<PermissionResponse>> getPendingPermissionByManagerId(@PathVariable(name = "manager_id") Long managerId,
                                                                               @RequestParam("status") PermissionStatus permissionStatus){
        return permissionApprovalService.getPendingPermissionByManagerId(managerId, permissionStatus);
    }

    @PostMapping("/confirmation")
    public PermissionResponse permissionConfirmation(@RequestBody PermissionConfirmationRequest permissionConfirmationRequest) {
        return permissionApprovalService.permissionConfirmation(permissionConfirmationRequest);
    }


}
