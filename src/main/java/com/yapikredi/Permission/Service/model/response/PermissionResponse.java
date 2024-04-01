package com.yapikredi.Permission.Service.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionResponse {
    private BigDecimal amount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

}
