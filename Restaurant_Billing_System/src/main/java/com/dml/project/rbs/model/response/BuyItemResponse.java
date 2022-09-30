package com.dml.project.rbs.model.response;

import com.dml.project.rbs.entity.BuyItem;
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
    private List<BuyItem> buyItem;
    private int totalBill;

}
