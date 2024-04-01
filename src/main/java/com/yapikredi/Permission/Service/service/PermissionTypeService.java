package com.yapikredi.Permission.Service.service;


import com.yapikredi.Permission.Service.persistence.entity.PermissionTypeEntity;
import com.yapikredi.Permission.Service.persistence.repository.PermissionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionTypeService {
    private final PermissionTypeRepository permissionTypeRepository;

    public List<PermissionTypeEntity> getAllPermissionType() {
        return permissionTypeRepository.findAll();
    }


}
