package com.StackDash.ServiceImpl;

import com.StackDash.Entity.OtpEntity;
import com.StackDash.Repository.OtpRepository;
import com.StackDash.Service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public String generateAndSaveOtp(String email) {
        try {
            String otp = generateOtp();
            Instant now = Instant.now();
            Instant expiry = now.plus(Duration.ofMinutes(5));

            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setOtpCode(otp);
            otpEntity.setEmail(email);
            otpEntity.setCreatedAt(now);
            otpEntity.setExpiresAt(expiry);
            otpEntity.setVerified(false);

            otpRepository.save(otpEntity);
            return otp;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return "Unable to generate Otp";
        }
    }

//    @Override
//    public boolean validateOtp(String email, String otpCode) {
//        try {
//            Optional<OtpEntity> otpEntry = otpRepository.findByEmailAndOtpCode(email, otpCode);
//
//            if (otpEntry.isEmpty()) return false;
//            OtpEntity entry = otpEntry.get();
//
//            if (entry.isVerified()) return false;
//            if (Instant.now().isAfter(entry.getExpiresAt())) ;
//
//            entry.setVerified(true);
//            otpRepository.save(entry);
//            return true;
//        }catch (Exception ex){
//            System.out.println(ex.getMessage());
//            return false;
//        }
//    }

    @Override
    public ResponseEntity<?> validateOtp(String email, String otpCode) {
        try {
            Optional<OtpEntity> otpEntry = otpRepository.findByEmailAndOtpCode(email, otpCode);

            if (otpEntry.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            OtpEntity entry = otpEntry.get();

            if (entry.isVerified()) {

                return ResponseEntity.badRequest().body("User Already registered ");
            }
            if (Instant.now().isAfter(entry.getExpiresAt())) ;

            entry.setVerified(true);
            otpRepository.save(entry);
            return ResponseEntity.ok().body(true);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return ResponseEntity.ok(false);
        }
    }
    @Override
    public String generateOtp() {
        return String.format("%06d", new SecureRandom().nextInt(100000));
    }


}
