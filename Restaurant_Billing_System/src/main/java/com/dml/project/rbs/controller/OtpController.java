package com.dml.project.rbs.controller;

import com.dml.project.rbs.model.request.OtpRequest;
import com.dml.project.rbs.model.request.ForgotPasswordRequest;
import com.dml.project.rbs.service.Impl.OTPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/rbs")
@RestController
public class OtpController {
    @Autowired
    private OTPServiceImpl otpServiceImpl;

    @PostMapping({"/generateOtp"})
    public ResponseEntity<Object> generateOtp(@RequestBody @Valid OtpRequest otpRequest){

        Object returnValue = otpServiceImpl.sendOtpPasswordReset(otpRequest);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }


    @PostMapping({"/forgotPassword"})
    public ResponseEntity<String>forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest){

      String returnValue = otpServiceImpl.ForgotPasswordOtp(forgotPasswordRequest);

      return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }
}
