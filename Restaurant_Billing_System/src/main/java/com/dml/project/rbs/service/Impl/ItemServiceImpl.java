package com.dml.project.rbs.service.Impl;


import com.dml.project.rbs.dto.OrderDto;
import com.dml.project.rbs.dto.UpdateItemDto;
import com.dml.project.rbs.entity.OrdersEntity;
import com.dml.project.rbs.entity.ItemEntity;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.exception.ItemNotFoundException;
import com.dml.project.rbs.dto.ItemDto;
import com.dml.project.rbs.model.response.BuyItemResponse;
import com.dml.project.rbs.model.response.ItemResponse;
import com.dml.project.rbs.model.response.MessageResponse;
import com.dml.project.rbs.repository.OrdersRepository;
import com.dml.project.rbs.repository.ItemRepository;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.ItemService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;


    public int totalBill = 0;
    @Autowired
    private OrdersRepository ordersRepository;




    public boolean flag = true;

    @Override
    public MessageResponse addItems(ItemDto itemDto) {


        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ItemEntity itemEntity = modelMapper.map(itemDto,ItemEntity.class);

        itemRepository.save(itemEntity);

        return new MessageResponse(itemDto.getName()+" added Successfully!!");
    }


    public MessageResponse addAllItems(List<ItemDto> itemDto){

            List<ItemEntity> itemEntity = new ArrayList<>();


            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            itemDto.forEach(item -> {
                itemEntity.add(modelMapper.map(item,ItemEntity.class));
            });

            itemRepository.saveAll(itemEntity);
            return new MessageResponse(itemDto.size()+" Items Added Successfully!!");

    }



    public ItemResponse getItems(Integer pageNumber, Integer pageSize) {

        Pageable p = PageRequest.of(pageNumber,pageSize);


        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Page<ItemEntity> pageItems = itemRepository.findAll(p);
        List<ItemEntity> content = pageItems.getContent();


        if(content==null){
            throw new ItemNotFoundException("No Items Found!");
        }

        List<ItemDto> itemDtoList = new ArrayList<>();

        content.forEach(item -> {

            if(!item.isDeleted())
                itemDtoList.add(modelMapper.map(item,ItemDto.class));
        });

         return new ItemResponse(itemDtoList,itemDtoList.size()+" items found!");

    }

    public ItemDto getItemsById(long id) {



        ItemEntity items =  itemRepository.findById(id).orElse(null);
        if(items == null || items.isDeleted()){
            throw new ItemNotFoundException("Item Could Not be Found!");
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ItemDto returnValue = modelMapper.map(items, ItemDto.class);
        return returnValue;
    }
    public ItemResponse getItemsByName(String name){

        List<ItemEntity> items = itemRepository.startsWithName(name);

        if(items== null || items.isEmpty()){
            throw  new ItemNotFoundException("Item with name "+name+" Does not exists!");
        }

        List<ItemDto> itemDtoList = new ArrayList<>();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        items.forEach(item -> {
            if(!item.isDeleted())
            {
                itemDtoList.add(modelMapper.map(item,ItemDto.class));
            }
            else{
                throw  new ItemNotFoundException("Item with name "+name+" Does not exists!");
            }

        });

        return new ItemResponse(itemDtoList,items.size()+" items Found!");
    }

    public MessageResponse deleteItemById(long id) {
        ItemEntity itemEntity = itemRepository.findById(id).orElse(null);
        if(itemEntity ==null || itemEntity.isDeleted()){
            throw new ItemNotFoundException("Item Doesnot Exist!");
        }
        itemEntity.setDeleted(true);
        itemRepository.save(itemEntity);
        return new MessageResponse("Item Deleted with id:"+id);
    }

    public BuyItemResponse buyFoodItems(List<OrderDto> orderDto, String email) {

        UserEntity existingUserEntity = userRepository.findByEmail(email);
        
        
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        List<OrdersEntity> orderEntity = new ArrayList<>();
    

        orderDto.forEach(order -> {
            orderEntity.add(modelMapper.map(order,OrdersEntity.class));
        });

        try{
            orderEntity.forEach(buyitem -> {
                String name = buyitem.getName();
                ItemEntity itemEntity = itemRepository.findByName(name);
                if(itemEntity.isDeleted()){
                    throw new ItemNotFoundException(itemEntity.getName()+" is not available! Please try Again!");
                }
                int total = buyitem.getQty() * itemEntity.getPrice();
                buyitem.setPrice(itemEntity.getPrice());
                buyitem.setAmount(total);
            });


            existingUserEntity.setOrders(orderEntity);
            userRepository.save(existingUserEntity);

        }
        catch (NullPointerException e){
            throw new ItemNotFoundException("Error Occurred while buying items.Please enter the items from the menu!");
        }




        int bill = getTotalBill(orderEntity);

        return new BuyItemResponse(orderEntity.size()+" Items Bought!", bill);


    }

    @Override
    public List<OrderDto> listBoughtItems(String email) {
        UserEntity existingUserEntity = userRepository.findByEmail(email);
        List<OrdersEntity> ordersEntityList =  existingUserEntity.getOrders();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<OrderDto> orderDtos= new ArrayList<>();
        ordersEntityList.forEach(order -> orderDtos.add(modelMapper.map(order,OrderDto.class))
        );
        return orderDtos;

    }

    public MessageResponse updateItem(UpdateItemDto updateItemDto){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ItemEntity itemEntity = modelMapper.map(updateItemDto,ItemEntity.class);

        ItemEntity existingItemEntity = itemRepository.findById(itemEntity.getId()).orElse(null);
        if(existingItemEntity == null){
            throw new ItemNotFoundException("Item could Not be Found!");
        }
        existingItemEntity.setId(itemEntity.getId());
        existingItemEntity.setDesc(itemEntity.getDesc());
        existingItemEntity.setName(itemEntity.getName());
        existingItemEntity.setPrice(itemEntity.getPrice());

        itemRepository.save(existingItemEntity);
        return new MessageResponse("Item with Id: "+updateItemDto.getId()+"updated successfully!!");
    }



    @Override
    public Object generatePdf(String email) throws FileNotFoundException, JRException {

        UserEntity existingUserEntity = userRepository.findByEmail(email);
        List<OrdersEntity> boughtItems = existingUserEntity.getOrders();

        int pdfBill = getTotalBill(boughtItems);

        if(pdfBill!=0){
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(boughtItems);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("total",pdfBill);
            parameters.put("uname", existingUserEntity.getFirstName());

            JasperReport compileReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/invoice.jrxml"));

            JasperPrint report = JasperFillManager.fillReport(compileReport,parameters,beanCollectionDataSource);

            JasperExportManager.exportReportToPdfFile(report,"src/main/resources/Restaurant Bill.pdf");
            byte[] data = JasperExportManager.exportReportToPdf(report);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=Restaurant Bill.pdf");



            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
        }

        throw  new ItemNotFoundException("User Has No Orders Yet!");

    }

    public int getTotalBill(List<OrdersEntity> boughtItems){
        totalBill = 0;
        boughtItems.forEach(buyitem -> {
            int amount = buyitem.getAmount();
            totalBill = totalBill + amount;
        });
        return totalBill;
    }


}
