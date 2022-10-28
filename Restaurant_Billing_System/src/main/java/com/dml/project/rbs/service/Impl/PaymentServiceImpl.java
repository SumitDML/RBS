package com.dml.project.rbs.service.Impl;

import com.dml.project.rbs.entity.OrdersEntity;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.response.PaymentResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.PaymentService;
import com.dml.project.rbs.util.JwtUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PaymentServiceImpl implements PaymentService {
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
    @Override
    public PaymentResponse createOrder(UserEntity existingUser) throws RazorpayException {
        PaymentResponse paymentResponse = new PaymentResponse();

            client = new RazorpayClient(SECRET_ID, SECRET_KEY);


            int total = getTotalBill(existingUser.getOrders());

            String username = existingUser.getFirstName()+" "+ existingUser.getLastName();

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
            paymentResponse.setEmail(existingUser.getEmail());
            paymentResponse.setContact(existingUser.getPhoneNumber());

        return paymentResponse;
    }

    public int getTotalBill(List<OrdersEntity> boughtItems){
        totalBill = 0;
        boughtItems.forEach(buyitem -> {
            int amount = buyitem.getAmount();
            totalBill = totalBill + amount;
        });
        return totalBill;
    }
    private Order createRazorPayOrder(int amount) throws RazorpayException {
        JSONObject options = new JSONObject();

        options.put("amount", amount*100);
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_11");
        options.put("payment_capture", 1);


        return client.orders.create(options);
    }
}
