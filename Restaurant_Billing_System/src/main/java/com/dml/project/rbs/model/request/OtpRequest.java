package com.dml.project.rbs.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OtpRequest {

    @NotNull(message = "Phone Number Should not be nul")
    private String phoneNumber;

    @Email
    @NotNull(message = "Email cannot be null")
    private String email;


}
