package com.yapikredi.Permission.Service.model.request;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionCreateRequest {
    private Long employeeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
