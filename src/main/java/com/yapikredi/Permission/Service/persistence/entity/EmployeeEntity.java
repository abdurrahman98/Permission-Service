package com.yapikredi.Permission.Service.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fisrt_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Column(name = "total_permission")
    private BigDecimal totalPermission;

    @Column(name = "identity_number")
    private String identityNumber;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private EmployeeEntity manager;

    @OneToMany(mappedBy = "employeeEntity", cascade = CascadeType.ALL)
    private List<PermissionEntity> permissionEntities;
}
