package com.yapikredi.Permission.Service.model.response;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String identityNumber;
    private LocalDateTime hireDate;
    private BigDecimal totalPermission;
}
