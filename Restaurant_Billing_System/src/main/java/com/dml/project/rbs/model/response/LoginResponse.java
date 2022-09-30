package com.dml.project.rbs.model.response;

import com.dml.project.rbs.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private User user;
    private String jwtToken;
}
