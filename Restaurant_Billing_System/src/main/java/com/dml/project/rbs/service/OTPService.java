package com.dml.project.rbs.service;

import com.dml.project.rbs.Redis.RedisUtility;
import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.model.request.OtpRequest;
import com.dml.project.rbs.model.request.ForgotPasswordRequest;
import com.dml.project.rbs.model.response.OTPStatus;
import com.dml.project.rbs.model.response.OtpResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class OTPService {


    @Autowired
    private RedisUtility redisUtility;

    @Autowired
    private UserRepository userRepository;
    @Value("${twilio.account_sid}")
    String accountSid;
    @Value("${twilio.auth_token}")
    String authToken;

    @Value("${twilio.trial_number}")
    String trialNumber;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public Object sendOtpPasswordReset(OtpRequest otpRequest) {
        Twilio.init(accountSid,authToken);

        User existingUser = userRepository.findByEmail(otpRequest.getEmail());
        if(existingUser!=null){
            String phone = existingUser.getPhoneNumber();
            if(phone.equals(otpRequest.getPhoneNumber())){
                try {
                    PhoneNumber to = new PhoneNumber(otpRequest.getPhoneNumber());
                    PhoneNumber from = new PhoneNumber(trialNumber);
                    String otp = generateOtp();
                    String otpMessage = "Dear User, Your OTP is: " + otp + ".Use this to reset your password.Thank You!";
                    Message message = Message.creator(to, from, otpMessage).create();
                    String mail_key = otpRequest.getEmail() + "_OTP";
                    redisUtility.setData(mail_key, otp);
                    return new OtpResponse(otpMessage, OTPStatus.DELIVERED);
                } catch (Exception e) {
                    return new OtpResponse(e.getMessage(), OTPStatus.FAILED);
                }
            }
            else {
                return new String("Invalid PhoneNumber");
            }
        }
        return new String("Email Invalid");

    }

    private String generateOtp() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }

    public String ForgotPasswordOtp(ForgotPasswordRequest forgotPasswordRequest) {
        String userOtp = forgotPasswordRequest.getOtp();
        String email = forgotPasswordRequest.getEmail();

        String key = email + "_OTP";
        String dbOTP = redisUtility.getData(key);


            if (dbOTP != null || userOtp != null && userOtp.equals(dbOTP)) {
                redisUtility.delData(key);
                if(forgotPasswordRequest.getNewPassword().equals(forgotPasswordRequest.getConfirmNewPassword())){
                    User existingUser = userRepository.findByEmail(email);
                    if(existingUser==null){
                        return "Invalid Email,Please Try Again!";
                    }
                    existingUser.setPassword(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()));
                    userRepository.save(existingUser);
                    return "Password Successfully Changed!";

                }
                else{
                    return "Password Does not match please Re-Enter!";
                }

            }
            return "OTP Verification Unsuccessful!";
    }
}