package com.dml.project.rbs.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.dml.project.rbs.entity.Orders;
import com.dml.project.rbs.entity.User;

import com.dml.project.rbs.model.response.PaymentResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.util.JwtUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;


@RestController
@RequestMapping("/RBS")
public class RazorpayController {
    private RazorpayClient client;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    //@Value("${razorpay.secret_id}")
    private static String SECRET_ID = "rzp_test_cKSX43UeLX3wNO";

    //@Value("${razorpay.secret_key}")
    private static String SECRET_KEY = "TgZCuGIgiTqN4Qnh8sW7ZWy3";

    private int totalBill;


    @PostMapping(path = "/payment")
    @PreAuthorize("hasAnyRole('Admin','User')")
    public PaymentResponse createOrder(@RequestHeader("Authorization") String TokenHeader) {
        PaymentResponse paymentResponse = new PaymentResponse();
    try{
            client = new RazorpayClient(SECRET_ID, SECRET_KEY);

            String jwtToken = TokenHeader.substring(7);
            String email = jwtUtil.extractEmailFromToken(jwtToken);
            User existingUser = userRepository.findByEmail(email);
            int total = getTotalBill(existingUser.getOrders());

            String username = existingUser.getFirstName()+" "+existingUser.getLastName();

            Order order = createRazorPayOrder(total);

            System.out.println("---------------------------");
            String orderId = order.get("id");
            System.out.println("Order ID: " + orderId);
            System.out.println("---------------------------");

            paymentResponse.setRazorpayOrderId(orderId);
            paymentResponse.setApplicationFee("" + total);
            paymentResponse.setSecretKey(SECRET_KEY);
            paymentResponse.setSecretId(SECRET_ID);
            paymentResponse.setPgName("Razorpay");
            paymentResponse.setName(username);
            paymentResponse.setEmail(email);
            paymentResponse.setContact(existingUser.getPhoneNumber());

        } catch (RazorpayException e) {

            e.printStackTrace();
        }

        return paymentResponse;

    }

    private Order createRazorPayOrder(int amount) throws RazorpayException {
        JSONObject options = new JSONObject();

        options.put("amount", amount*100);
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_11");
        options.put("payment_capture", 1);


        return client.orders.create(options);
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