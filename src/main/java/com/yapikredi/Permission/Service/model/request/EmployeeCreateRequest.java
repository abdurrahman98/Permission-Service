package com.yapikredi.Permission.Service.model.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreateRequest {

    private String firstName;
    private String lastName;
    private String identityNumber;

}
