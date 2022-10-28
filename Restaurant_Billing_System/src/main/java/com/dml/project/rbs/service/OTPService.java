package com.dml.project.rbs.service;

import com.dml.project.rbs.model.request.ForgotPasswordRequest;
import com.dml.project.rbs.model.request.OtpRequest;

public interface OTPService {
    Object sendOtpPasswordReset(OtpRequest otpRequest);
    String ForgotPasswordOtp(ForgotPasswordRequest forgotPasswordRequest);
}
