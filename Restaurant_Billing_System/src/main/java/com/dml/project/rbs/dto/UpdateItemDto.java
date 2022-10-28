package com.dml.project.rbs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemDto {
    @NotNull(message = "Id should not be null")
    private Long id;

    @NotBlank(message = "Name Should Not Be Blank")
    private String name;

    @NotBlank(message = "Description Should Not Be Blank")
    private String desc;

    @NotNull(message = "Price Should Not Be Null")
    private Integer price;
}
