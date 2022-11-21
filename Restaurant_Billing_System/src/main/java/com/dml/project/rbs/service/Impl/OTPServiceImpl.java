package com.dml.project.rbs.service.Impl;

import com.dml.project.rbs.Redis.RedisUtility;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.exception.InvalidArgumentException;
import com.dml.project.rbs.model.request.OtpRequest;
import com.dml.project.rbs.model.request.ForgotPasswordRequest;
import com.dml.project.rbs.model.response.MessageResponse;
import com.dml.project.rbs.model.response.OTPStatus;
import com.dml.project.rbs.model.response.OtpResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.OTPService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class OTPServiceImpl implements OTPService {


    @Autowired
    private RedisUtility redisUtility;

    @Autowired
    private UserRepository userRepository;
    //@Value("${twilio.account_sid}")
    String accountSid="AC2875d1cc43ea26bdbcfa5014f58585f7";
    //@Value("${twilio.auth_token}")
    String authToken="4241180f6990a39b8a48702fccfac034";

    //@Value("${twilio.trial_number}")
    String trialNumber="+15155171695";
    @Autowired
    private PasswordEncoder passwordEncoder;




    public Object sendOtpPasswordReset(OtpRequest otpRequest) {
        Twilio.init(accountSid,authToken);

        UserEntity existingUserEntity = userRepository.findByEmail(otpRequest.getEmail());
        if(existingUserEntity !=null){
            String phone = existingUserEntity.getPhoneNumber();
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
                    throw new InvalidArgumentException("Error Occured while sending OTP! Please Retry!");
                }
            }
            else {
                return new MessageResponse("Invalid PhoneNumber") ;
            }
        }
        return new MessageResponse("Email Invalid");

    }

    public Object sendOtpEmail(OtpRequest otpRequest) {
        Twilio.init(accountSid,authToken);

        UserEntity existingUserEntity = userRepository.findByEmail(otpRequest.getEmail());
        if(existingUserEntity !=null){
            String phone = existingUserEntity.getPhoneNumber();
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
                return "Invalid PhoneNumber";
            }
        }
        return "Email Invalid";

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
                    UserEntity existingUserEntity = userRepository.findByEmail(email);
                    if(existingUserEntity ==null){
                        return "Invalid Email,Please Try Again!";
                    }
                    existingUserEntity.setPassword(passwordEncoder.encode(forgotPasswordRequest.getNewPassword()));
                    userRepository.save(existingUserEntity);
                    return "Password Successfully Changed!";

                }
                else{
                    return "Password Does not match please Re-Enter!";
                }

            }
            return "OTP Verification Unsuccessful! Try Again";
    }
}