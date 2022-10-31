package com.dml.project.rbs.controller;

import com.dml.project.rbs.dto.UserProfileDto;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.request.SignUpRequest;
import com.dml.project.rbs.model.response.MessageResponse;
import com.dml.project.rbs.model.response.ResponseModel;
import com.dml.project.rbs.service.UserService;
import com.dml.project.rbs.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;


@RestController
@RequestMapping("/rbs")
public class UserController {
    @Autowired
    private Environment env;

    @Autowired
    UserService userService;



    @PostConstruct
    public void initRolesController(){

        userService.initRolesAndUser();
    }


    @PostMapping({"/register"})
    public ResponseEntity registerNewUser(@Valid @RequestBody SignUpRequest signUpRequest){


        try{
            MessageResponse returnValue = userService.registerNewUser(signUpRequest);
            return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
        }
        catch (EntityNotFoundException | ConstraintViolationException e){
            return new ResponseEntity(new ResponseModel<>(HttpStatus.BAD_REQUEST,e.getMessage(),null,null), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping({"/getUserDetails"})
    public ResponseEntity userProfile(@RequestHeader("Authorization") String tokenheader){

        UserProfileDto returnValue = userService.userProfile(tokenheader);
        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping({"/clearOrderHistory"})
    public ResponseEntity<Object> clearRecord(@RequestHeader("Authorization") String tokenHeader){

        MessageResponse returnValue = userService.clearOrderHistory(tokenHeader);
        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK,null,null,returnValue), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping({"/status/check"})
    public ResponseEntity<String> StatusCheck(){
       String returnValue = env.getProperty("test.status");

        return new ResponseEntity("Status : "+ returnValue,HttpStatus.OK);
    }





}
