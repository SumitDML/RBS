package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.response.PaymentResponse;
import com.razorpay.RazorpayException;
import org.springframework.web.bind.annotation.RequestHeader;

public interface PaymentService {
    PaymentResponse createOrder(UserEntity existingUser) throws RazorpayException;
}
