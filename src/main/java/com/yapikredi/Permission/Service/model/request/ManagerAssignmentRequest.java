package com.yapikredi.Permission.Service.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerAssignmentRequest {
    private Long employeeId;
    private Long managerId;

}
