package com.dml.project.rbs.controller;

import com.dml.project.rbs.dto.OrderDto;
import com.dml.project.rbs.dto.UpdateItemDto;
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

import java.util.List;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;


import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
    @Cacheable("ItemList")
    @GetMapping(path = "/listItems",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseModel<ItemResponse> getAllItems(@RequestParam(value = "pageNumber",defaultValue = "0",required = false)Integer pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "5",required = false)Integer pageSize) {
//        sleep(1);
//        count++;
//        System.out.println("List All items Called!! "+count);
        ItemResponse returnValue = itemService.getItems(pageNumber,pageSize);
        return  new ResponseModel<ItemResponse>(HttpStatus.OK,null,null,returnValue);

    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(path = "/searchItemByName",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    @CacheEvict(value = "List",allEntries = true)
    public ResponseEntity getItemsByName(@RequestParam @NotBlank(message = "Name should not be blank/null") String name){

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
    public ResponseEntity buyItems(@RequestHeader("Authorization") String TokenHeader , @Valid @RequestBody List<OrderDto> orders){

        String jwtToken = TokenHeader.substring(7);
        String email = jwtUtil.extractEmailFromToken(jwtToken);
        BuyItemResponse returnValue =  itemService.buyFoodItems(orders,email);

        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(path = "/getAllBoughtItems",
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseModel ListBoughtItems(@RequestHeader("Authorization") String TokenHeader){
        String jwtToken = TokenHeader.substring(7);

        String email = jwtUtil.extractEmailFromToken(jwtToken);

        ItemResponse returnValue =  itemService.listBoughtItems(email);
        return new ResponseModel<>(HttpStatus.OK,null,null,returnValue);
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
