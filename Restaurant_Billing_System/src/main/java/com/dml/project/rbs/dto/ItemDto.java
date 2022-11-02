package com.dml.project.rbs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemDto implements Serializable {

    @NotBlank(message = "Name Should Not Be Blank")
    private String name;

    @NotBlank(message = "Description Should Not Be Blank")
    private String desc;

    @NotNull(message = "Price Should Not Be Blank")
    private Integer price;
}
