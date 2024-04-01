package com.yapikredi.Permission.Service.persistence.repository;

import com.yapikredi.Permission.Service.persistence.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EmployeeEntity e where e.id=:id")
    Optional<EmployeeEntity> findByIdLock(Long id);

    List<EmployeeEntity> findByManagerId(Long id);

    @Query("SELECT e FROM EmployeeEntity e WHERE DAY(e.hireDate) = :day AND MONTH(e.hireDate) = :month")
    List<EmployeeEntity> findEmployeesByHireDateDayAndMonth(Integer day, Integer month);
}
