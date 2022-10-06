package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.model.request.SignUpRequest;
import com.dml.project.rbs.service.ItemService;
import com.dml.project.rbs.service.UserService;
import com.dml.project.rbs.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping("/RBS")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @PostConstruct
    public void initRolesController(){
        userService.initRolesAndUser();
    }


    @PostMapping({"/register"})
    public ResponseEntity<Object> registerNewUser(@RequestBody @Valid SignUpRequest signUpRequest){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User user = modelMapper.map(signUpRequest,User.class);
        User user1 = userService.registerNewUser(user);
        if(user1==null){
            return new ResponseEntity("User Already Exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(user1,HttpStatus.OK);
    }


}
