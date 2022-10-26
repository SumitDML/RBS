package com.dml.project.rbs.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String secretKey;
    private String razorpayOrderId;
    private String applicationFee;
    private String secretId;
    private String pgName;
    private String name;
    private String contact;
    private String email;




}
