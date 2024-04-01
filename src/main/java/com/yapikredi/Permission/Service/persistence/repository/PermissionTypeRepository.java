package com.yapikredi.Permission.Service.persistence.repository;

import com.yapikredi.Permission.Service.persistence.entity.PermissionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionTypeRepository extends JpaRepository<PermissionTypeEntity, Long> {
}
