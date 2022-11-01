package com.dml.project.rbs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {
    private long id;
    private String name;
    private int qty;
    private int price;
    private int amount;

}
