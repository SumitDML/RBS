package com.dml.project.rbs.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgotPasswordRequest {
    //@NotNull(message = "OTP Cannot be null")
    private String Otp;
//    @Email
//    @NotNull(message = "Email Cannot be null")
    private String email;
   // @NotNull(message = "New Password  Cannot be null")
    private String newPassword;
    //@NotNull(message = "Confirm Password Cannot be null")
    private String confirmNewPassword;
}
