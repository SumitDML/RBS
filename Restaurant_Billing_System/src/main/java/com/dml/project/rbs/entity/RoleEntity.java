package com.dml.project.rbs.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class RoleEntity implements Serializable {
    @Id
    private String roleName;
    private String roleDescription;

}
