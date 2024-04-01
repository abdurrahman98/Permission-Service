package com.yapikredi.Permission.Service.persistence.repository;

import com.yapikredi.Permission.Service.model.enums.PermissionStatus;
import com.yapikredi.Permission.Service.persistence.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from PermissionEntity e where e.id=:id")
    Optional<PermissionEntity> findByIdLock(Long id);

    List<PermissionEntity> findByEmployeeEntity_Id(Long id);

    List<PermissionEntity> findByEmployeeEntity_IdAndStatus(Long id, PermissionStatus permissionStatus);

    List<PermissionEntity> findByEmployeeEntity_Manager_Id(Long id);
}
