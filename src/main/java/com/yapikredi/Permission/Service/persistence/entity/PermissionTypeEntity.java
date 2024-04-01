package com.yapikredi.Permission.Service.persistence.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission_type")
public class PermissionTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "start_years")
    private Long startYears;

    @Column(name = "end_years")
    private Long endYears;

    @Column(name = "name")
    private String name;
}
