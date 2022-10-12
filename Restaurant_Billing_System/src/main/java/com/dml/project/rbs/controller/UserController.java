package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.model.request.SignUpRequest;
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
import javax.validation.Valid;


@RestController
@RequestMapping("/RBS")
public class UserController {
    @Autowired
    private Environment env;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;


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

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping({"/getUserDetails"})
    public ResponseEntity<Object> userProfile(@RequestHeader("Authorization") String TokenHeader){
        String jwtToken = TokenHeader.substring(7);
        String email = jwtUtil.extractEmailFromToken(jwtToken);
        User returnValue = userService.userProfile(email);
        return new ResponseEntity(returnValue,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping({"/clearOrderHistory"})
    public ResponseEntity<Object> clearRecord(@RequestHeader("Authorization") String TokenHeader){
        String jwtToken = TokenHeader.substring(7);
        String email = jwtUtil.extractEmailFromToken(jwtToken);
        String returnValue = userService.clearOrderHistory(email);

        return new ResponseEntity(returnValue,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping({"/status/check"})
    public ResponseEntity<String> StatusCheck(){
       String returnValue = env.getProperty("test.status");

        return new ResponseEntity("Status : "+ returnValue,HttpStatus.OK);
    }





}
