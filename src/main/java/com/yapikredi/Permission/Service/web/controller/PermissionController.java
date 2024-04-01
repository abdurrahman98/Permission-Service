package com.yapikredi.Permission.Service.web.controller;


import com.yapikredi.Permission.Service.model.request.PermissionCreateRequest;
import com.yapikredi.Permission.Service.model.response.PermissionResponse;
import com.yapikredi.Permission.Service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/all")
    public Map<Long, List<PermissionResponse>> getAllPermission() {
        return permissionService.getAll();
    }

    @GetMapping("/{employee_id}")
    public List<PermissionResponse> getByEmployeeId(@PathVariable("employee_id") Long employeeId) {
        return permissionService.getByEmployeeId(employeeId);
    }


    @PostMapping("/create")
    public PermissionResponse createPermission(@RequestBody PermissionCreateRequest permissionCreateRequest) {
        return permissionService.createPermission(permissionCreateRequest);
    }
}
