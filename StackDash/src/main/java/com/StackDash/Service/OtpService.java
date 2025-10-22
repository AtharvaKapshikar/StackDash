package com.StackDash.Service;

import org.springframework.http.ResponseEntity;

public interface OtpService {

    public String generateAndSaveOtp(String email);

    public ResponseEntity<?> validateOtp(String email, String otpCode);

    public String generateOtp();
}
