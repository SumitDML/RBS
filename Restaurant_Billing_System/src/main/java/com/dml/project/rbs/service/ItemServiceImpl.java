package com.dml.project.rbs.service;


import com.dml.project.rbs.entity.BuyItem;
import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.dml.project.rbs.repository.BuyItemRepository;
import com.dml.project.rbs.repository.ItemRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private BuyItemRepository buyItemRepository;


    public int totalBill = 0;

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
    public Item getItemsByName(String name){

        Item item = itemRepository.findByName(name);
        if(item!=null){
            return item;
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

    public BuyItemResponse buyFoodItems(List<BuyItem> buyItem) {

        List<BuyItem> boughtItems = null;
        try {
            buyItem.forEach(buyitem -> {

                String name = buyitem.getName();
                Item item = itemRepository.findByName(name);
                int total = buyitem.getQty() * item.getPrice();
                totalBill = totalBill + total;
                buyitem.setPrice(item.getPrice());
                buyitem.setAmount(total);
            });


            boughtItems = buyItemRepository.saveAll(buyItem);


        } catch (Exception e){
            totalBill=0;
            return null;
        }


        return new BuyItemResponse(boughtItems, totalBill);


    }

    @Override
    public List<BuyItem> listBoughtItems() {


        return buyItemRepository.findAll();
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
    public ResponseEntity<byte[]> generatePdf() throws FileNotFoundException, JRException {
        List<BuyItem> boughtItems = buyItemRepository.findAll();
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(boughtItems);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("total",totalBill);


        JasperReport compileReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/invoice.jrxml"));

        JasperPrint report = JasperFillManager.fillReport(compileReport,parameters,beanCollectionDataSource);

        JasperExportManager.exportReportToPdfFile(report,"Restaurant Bill.pdf");
        byte[] data = JasperExportManager.exportReportToPdf(report);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=Restaurant Bill.pdf");



        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);

    }


}
