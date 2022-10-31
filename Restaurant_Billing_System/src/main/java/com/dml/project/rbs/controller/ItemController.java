package com.dml.project.rbs.controller;

import com.dml.project.rbs.dto.CustomResponseEntity;
import com.dml.project.rbs.dto.OrderDto;
import com.dml.project.rbs.dto.UpdateItemDto;
import com.dml.project.rbs.entity.OrdersEntity;
import com.dml.project.rbs.entity.ItemEntity;
import com.dml.project.rbs.exception.FileNotFoundException;
import com.dml.project.rbs.exception.ValidationException;
import com.dml.project.rbs.model.response.ResponseModel;
import com.dml.project.rbs.dto.ItemDto;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.dml.project.rbs.model.response.ItemResponse;
import com.dml.project.rbs.model.response.MessageResponse;
import com.dml.project.rbs.service.ItemService;
import com.dml.project.rbs.util.JwtUtil;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;


import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

@RestController
@RequestMapping("/rbs")
@Validated
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private JwtUtil jwtUtil;
    int count = 0;
    int count2 = 0;

    @PreAuthorize("hasRole('Admin')")
    @CacheEvict(value = "List",allEntries = true)
    @PostMapping(path = "/addItem",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity addItem(@Valid @RequestBody ItemDto itemDto){

        try{
            MessageResponse returnValue = itemService.addItems(itemDto);
            return new ResponseEntity(new ResponseModel<>(HttpStatus.CREATED,null,null,returnValue), HttpStatus.CREATED);
        }
        catch (ValidationException e){
            return new ResponseEntity(new ResponseModel<>(HttpStatus.BAD_REQUEST,e.getMessage(),null,null), HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('Admin')")
    @CacheEvict(value = "List",allEntries = true)
    @PostMapping(path = "/addAllItems",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity addItem(@Valid @RequestBody List< ItemDto> itemDtos){
        try{
            MessageResponse returnValue = itemService.addAllItems(itemDtos);
            return new ResponseEntity(new ResponseModel<>(HttpStatus.CREATED,null,null,returnValue), HttpStatus.CREATED);
        }
        catch (ValidationException e){
            return new ResponseEntity(new ResponseModel<>(HttpStatus.BAD_REQUEST,e.getMessage(),null,null), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyRole('Admin','User')")
    @Cacheable("List")
    @GetMapping(path = "/listItems",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public CustomResponseEntity getAllItems() {
//        sleep(1);
//        count++;
//        System.out.println("List All items Called!! "+count);
        ItemResponse returnValue = itemService.getItems();
        return new CustomResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(path = "/searchItemByName/{name}",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    @CacheEvict(value = "List",allEntries = true)
    public ResponseEntity getItemsByName(@PathVariable String name){
//        count2++;
//        System.out.println("Search Item By Name Called!! "+count2);
        ItemResponse returnValue = itemService.getItemsByName(name);
        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @CacheEvict(value = "List",allEntries = true)
    @PutMapping(path = "/updateItem",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity updateItems(@Valid @RequestBody UpdateItemDto updateItemDto){
        MessageResponse returnValue = itemService.updateItem(updateItemDto);
        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping(path = "/deleteItem")
    public ResponseEntity  deleteItems(@RequestParam Long id){
        MessageResponse returnValue = itemService.deleteItemById(id);
        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @PostMapping(path = "/buyItems",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    @CacheEvict(value = "BuyItems",allEntries = true)
    public ResponseEntity buyItems(@RequestHeader("Authorization") String TokenHeader , @RequestBody List<OrderDto> orders){

        String jwtToken = TokenHeader.substring(7);

        String email = jwtUtil.extractEmailFromToken(jwtToken);
        System.out.println(email);

        BuyItemResponse returnValue =  itemService.buyFoodItems(orders,email);

        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @Cacheable("BuyList")
    @GetMapping(path = "/getAllBoughtItems",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public CustomResponseEntity ListBoughtItems(@RequestHeader("Authorization") String TokenHeader){
        String jwtToken = TokenHeader.substring(7);

        String email = jwtUtil.extractEmailFromToken(jwtToken);

        sleep(1);
        System.out.println("Buy List Called!");
        List<OrderDto> returnValue =  itemService.listBoughtItems(email);
        return new CustomResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @GetMapping("/pdf")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public Object generatePdf(@RequestHeader("Authorization") String TokenHeader ) {
        String jwtToken = TokenHeader.substring(7);

        String email = jwtUtil.extractEmailFromToken(jwtToken);
        try {
            Object returnValue =  itemService.generatePdf(email);
            return returnValue;
        }
        catch(FileNotFoundException | java.io.FileNotFoundException | JRException e){
            throw new FileNotFoundException("Some Error Occurred while generating PDF!");
        }
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
