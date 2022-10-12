package com.dml.project.rbs.model.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @NotNull(message = "First Name cannot be null")
    @Size(min=2,message="First name must not be less than two characters")
    private String firstName;
    @NotNull(message = "last Name cannot be null")
    @Size(min=2,message="last name must not be less than two characters")
    private String lastName;
    @NotNull(message = "password should not be null")
    @Size(min=4,max=20,message="Length must be between 8-16")
    private String password;
    @Email
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "PhoneNumber should not be null")
    private String phoneNumber;

}
