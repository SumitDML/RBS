package com.dml.project.rbs.model.response;

import com.dml.project.rbs.entity.OrdersEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyItemResponse {
    private String message;
    private int totalBill;

}
