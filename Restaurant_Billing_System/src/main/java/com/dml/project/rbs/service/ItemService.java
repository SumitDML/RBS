package com.dml.project.rbs.service;



import com.dml.project.rbs.entity.Orders;
import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.model.response.BuyItemResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.util.List;


public interface ItemService  {
    public List<Item> getItems();
    public Item addItems(Item item);
    public List<Item> addAllItems(List<Item> item);
    public Item getItemsById(long id);
    public List<Item> getItemsByName(String name);
    public String deleteItemById(long id);
    public Item updateItem(Item item);
    public BuyItemResponse buyFoodItems(List<Orders> ordersRequest, String email);

    public ResponseEntity<byte[]> generatePdf(String email) throws FileNotFoundException, JRException;
    public List<Orders> listBoughtItems(String email);


}
