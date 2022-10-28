package com.dml.project.rbs.controller;

import com.dml.project.rbs.entity.UserEntity;

import com.dml.project.rbs.model.response.ResponseModel;
import com.dml.project.rbs.model.response.PaymentResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.PaymentService;
import com.dml.project.rbs.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;


@RestController
@RequestMapping("/rbs")
public class RazorpayController {
    private RazorpayClient client;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;

    //@Value("${razorpay.secret_id}")
    private static String SECRET_ID = "rzp_test_cKSX43UeLX3wNO";

    //@Value("${razorpay.secret_key}")
    private static String SECRET_KEY = "TgZCuGIgiTqN4Qnh8sW7ZWy3";

    private int totalBill;




    @PostMapping(path = "/payment")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public ResponseEntity createOrder(@RequestHeader("Authorization") String TokenHeader) {

        try{
            client = new RazorpayClient(SECRET_ID, SECRET_KEY);

            String jwtToken = TokenHeader.substring(7);
            String email = jwtUtil.extractEmailFromToken(jwtToken);
            UserEntity existingUserEntity = userRepository.findByEmail(email);
            PaymentResponse returnValue = paymentService.createOrder(existingUserEntity);
            return new ResponseEntity(new ResponseModel<>(HttpStatus.OK, "Payment Order Created!", null, returnValue),HttpStatus.OK);

        } catch (RazorpayException e) {

            return new ResponseEntity(new ResponseModel<>(HttpStatus.OK, e.getMessage(), null, null),HttpStatus.OK);
        }

    }



}