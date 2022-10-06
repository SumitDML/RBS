package com.dml.project.rbs.service;


import com.dml.project.rbs.entity.Orders;
import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.dml.project.rbs.repository.OrdersRepository;
import com.dml.project.rbs.repository.ItemRepository;
import com.dml.project.rbs.repository.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;


    public int totalBill = 0;
    @Autowired
    private OrdersRepository ordersRepository;




    public boolean flag = true;

    @Override
    public Item addItems(Item item) {
        return itemRepository.save(item);
    }
    public List<Item> addAllItems(List<Item> item) {

        return itemRepository.saveAll(item);
    }

    public List<Item> getItems() {

        return itemRepository.findAll();
    }

    public Item getItemsById(long id) {

        return itemRepository.findById(id).orElse(null);
    }
    public List<Item> getItemsByName(String name){

        List<Item> items = itemRepository.startsWithName(name);

        if(items!=null){
            return items;
        }
        else{
            return null;
            //throw new NoSuchElementException("No Item Found with name : "+name);
        }

    }

    public String deleteItemById(long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if(item==null){
            return null;
        }
        itemRepository.deleteById(id);
        return "Item Deleted with id:"+id ;
    }

    public BuyItemResponse buyFoodItems(List<Orders> orders, String email) {

        User existingUser  = userRepository.findByEmail(email);

        try {
            orders.forEach(buyitem -> {
                String name = buyitem.getName();
                Item item = itemRepository.findByName(name);
                int total = buyitem.getQty() * item.getPrice();
                buyitem.setPrice(item.getPrice());
                buyitem.setAmount(total);
            });


            existingUser.setOrders(orders);
            userRepository.save(existingUser);


        } catch (Exception e){

            return null;
        }
        int bill = getTotalBill(orders);

        return new BuyItemResponse(orders, bill);


    }

    @Override
    public List<Orders> listBoughtItems() {


        return ordersRepository.findAll();
    }

    public Item updateItem(Item item){
        Item existingItem = itemRepository.findById(item.getId()).orElse(null);
        if(existingItem == null){
            return null;
        }
        existingItem.setId(item.getId());
        existingItem.setDesc(item.getDesc());
        existingItem.setName(item.getName());
        existingItem.setPrice(item.getPrice());
        return itemRepository.save(existingItem);
    }



    @Override
    public ResponseEntity<byte[]> generatePdf(String email) throws FileNotFoundException, JRException {
        List<Orders> boughtItems = userRepository.findByEmail(email).getOrders();

        int pdfBill = getTotalBill(boughtItems);

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(boughtItems);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total",pdfBill);

        JasperReport compileReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/invoice.jrxml"));

        JasperPrint report = JasperFillManager.fillReport(compileReport,parameters,beanCollectionDataSource);

        JasperExportManager.exportReportToPdfFile(report,"src/main/resources/Restaurant Bill.pdf");
        byte[] data = JasperExportManager.exportReportToPdf(report);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=Restaurant Bill.pdf");



        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);

    }

    public int getTotalBill(List<Orders> boughtItems){
        totalBill = 0;
        boughtItems.forEach(buyitem -> {
            int amount = buyitem.getAmount();
            totalBill = totalBill + amount;
        });
        return totalBill;
    }


}
