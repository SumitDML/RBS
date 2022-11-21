package com.dml.project.rbs.model.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.intellij.lang.annotations.RegExp;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "First Name cannot be Null/Blank")
    private String firstName;

    @NotBlank(message = "last Name cannot be Null/Blank")
    private String lastName;

    @NotBlank(message = "password should not be Null/Blank")
    @Size(min=4,max=20,message="Length must be between 8-16")
    private String password;


    @Pattern(message = "Email is not valid", regexp="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "Email cannot be Null/Blank")
    private String email;

    @NotBlank(message = "PhoneNumber should not be Null/Blank")
    @Size(min = 10,max = 13,message = "Phone Number Should be Of 10 digits")
    private String phoneNumber;

}
