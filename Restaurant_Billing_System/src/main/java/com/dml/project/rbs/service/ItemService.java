package com.dml.project.rbs.service;



import com.dml.project.rbs.entity.BuyItem;
import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.google.zxing.WriterException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public interface ItemService  {
    public List<Item> getItems();
    public Item addItems(Item item);
    public List<Item> addAllItems(List<Item> item);
    public Item getItemsById(long id);
    public List<Item> getItemsByName(String name);
    public String deleteItemById(long id);
    public Item updateItem(Item item);
    public BuyItemResponse buyFoodItems(List<BuyItem> buyItemRequest);

    public ResponseEntity<byte[]> generatePdf() throws FileNotFoundException, JRException;
    public List<BuyItem> listBoughtItems();


}
