package com.dml.project.rbs.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private String customerName;
    private String email;
    private String phoneNumber;
    private BigInteger amount;

}