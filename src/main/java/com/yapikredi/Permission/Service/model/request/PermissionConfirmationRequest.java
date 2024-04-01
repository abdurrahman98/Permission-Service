package com.yapikredi.Permission.Service.model.request;


import com.yapikredi.Permission.Service.model.enums.ConfirmationStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionConfirmationRequest {

    private Long employeeId;
    private Long managerId;
    private Long permissionId;
    private ConfirmationStatus confirmationStatus;
}
