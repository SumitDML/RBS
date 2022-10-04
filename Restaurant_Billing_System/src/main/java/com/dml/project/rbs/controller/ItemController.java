package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.BuyItem;
import com.dml.project.rbs.entity.Item;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.dml.project.rbs.service.ItemService;
import com.dml.project.rbs.service.ItemServiceImpl;
import com.google.zxing.WriterException;
import net.sf.jasperreports.components.barcode4j.QRCodeSVGImageProducer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;


import com.google.zxing.WriterException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/RBS")
public class ItemController {
    @Autowired
    private ItemService itemService;
    int count = 0;
    int count2 = 0;

    @PreAuthorize("hasRole('Admin')")
    @CacheEvict(value = "List",allEntries = true)
    @PostMapping(path = "/AddItem",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity<String> addItem(@RequestBody Item item){
        Item returnValue = itemService.addItems(item);

        return new ResponseEntity(returnValue, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('Admin')")
    @CacheEvict(value = "List",allEntries = true)
    @PostMapping(path = "/AddAllItems",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity<List<Item>> addItem(@RequestBody List<Item> item){

        List<Item> returnValue = itemService.addAllItems(item);

        return new ResponseEntity(returnValue,HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('Admin','User')")
    @Cacheable("List")
    @GetMapping(path = "/ListItems",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public Object getAllItems() {
        sleep(1);
        count++;
        System.out.println("List All items Called!! "+count);
        List<Item> returnValue = itemService.getItems();

        if(returnValue == null){
            return new String("Items Not Available!");
        }
        return returnValue;
       // return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(path = "/SearchItemByName/{name}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    @Cacheable(value = "Items",key = "#name")
    public Object getItemsByName(@PathVariable String name){
        count2++;
        System.out.println("Search Item By Name Called!! "+count2);
        List<Item> returnValue = itemService.getItemsByName(name);
        if(returnValue == null){
            return new String("Item with name: "+name+"Not Found!");
        }

        return returnValue;
    }

    @PreAuthorize("hasRole('Admin')")
    @CacheEvict(value = "List",allEntries = true)
    @PutMapping(path = "/UpdateItem",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public Object updateItems( @RequestBody Item item){
        Item returnValue = itemService.updateItem(item);
        if (returnValue == null) {
            return new String("Item with name: "+item.getName()+" Not Found!");

        }
        return returnValue;
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping(path = "/DeleteItem")
    public String  deleteItems(@RequestParam Long id){
        String returnValue = itemService.deleteItemById(id);
        if(returnValue == null){
            return ("Item with Id:"+id+"not Found!");
        }
        return returnValue;
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @PostMapping(path = "/BuyItems",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    @CacheEvict(value = "BuyItems",allEntries = true)
    public Object buyItems(@RequestBody List<BuyItem> buyItem){

        BuyItemResponse returnValue =  itemService.buyFoodItems(buyItem);
        if(returnValue == null){
            return new String("Invalid Input!!");
        }
        return returnValue;
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @Cacheable("BuyList")
    @GetMapping(path = "/GetAllBoughtItems",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public List<BuyItem> ListBoughtItems(){
        sleep(1);
        System.out.println("Buy List Called!");
        return  itemService.listBoughtItems();
    }

    @GetMapping("/pdf")
    @PreAuthorize("hasAnyRole('Admin')")
    public ResponseEntity<byte[]> generatePdf() throws JRException, FileNotFoundException {

       return  itemService.generatePdf();


    }

    private void sleep(int seconds){
        try {
            SECONDS.sleep(seconds);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
