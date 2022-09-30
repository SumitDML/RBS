package com.dml.project.rbs.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @Email
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "password should not be null")
    @Size(min=8,max=16,message="Length must be between 8-16")
    private String password;
}
