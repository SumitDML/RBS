package com.dml.project.rbs.service;



import com.dml.project.rbs.dto.OrderDto;
import com.dml.project.rbs.dto.UpdateItemDto;
import com.dml.project.rbs.entity.OrdersEntity;
import com.dml.project.rbs.dto.ItemDto;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.dml.project.rbs.model.response.ItemResponse;
import com.dml.project.rbs.model.response.MessageResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;


public interface ItemService  {
     ItemResponse getItems(Integer pageNumber,Integer pageSize);

     MessageResponse addItems(ItemDto itemDto);
     MessageResponse addAllItems(List<ItemDto> itemDtos);
     ItemDto getItemsById(long id);
     ItemResponse getItemsByName(String name);
     MessageResponse deleteItemById(long id);
     MessageResponse updateItem(UpdateItemDto updateItemDto);
     BuyItemResponse buyFoodItems(List<OrderDto> ordersEntityRequest, String email);

     Object generatePdf(String email) throws FileNotFoundException, JRException;
     ItemResponse listBoughtItems(String email);


}
