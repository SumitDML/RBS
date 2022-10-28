package com.dml.project.rbs.model.response;

import com.dml.project.rbs.dto.ItemDto;
import com.dml.project.rbs.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    List<ItemDto> menu;
    String result;
}
