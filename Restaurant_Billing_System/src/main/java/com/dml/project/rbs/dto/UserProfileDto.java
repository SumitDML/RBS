package com.dml.project.rbs.dto;

import com.dml.project.rbs.entity.OrdersEntity;
import com.dml.project.rbs.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private Set<RoleEntity> roleEntity = new HashSet<>();

    private List<OrdersEntity> orders = new ArrayList<>();
}
