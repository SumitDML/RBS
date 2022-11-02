package com.dml.project.rbs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {


    @NotBlank(message = "Name should not be blank/null")
    private String name;

    @NotNull(message = "Quantity should not be blank/null")
    private Integer qty;
    private Integer price;
    private Integer amount;

}
