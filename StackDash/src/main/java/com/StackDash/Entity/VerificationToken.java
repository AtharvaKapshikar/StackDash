package com.StackDash.Entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "StackDash_Verify_Token")
@SequenceGenerator(
        name = "stack_dash_token_seq",
        sequenceName = "stack_dash_token_seq",
        allocationSize = 50
)

public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stack_dash_token_seq")
    private Long id;
    private Integer otp;
    private LocalTime expirayTime;
    private Boolean verificationStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public LocalTime getExpirayTime() {
        return expirayTime;
    }

    public void setExpirayTime(LocalTime expirayTime) {
        this.expirayTime = expirayTime.plusMinutes(10);
    }

    public Boolean getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(Boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
                ", otp=" + otp +
                ", expirayTime=" + expirayTime +
                ", verificationStatus=" + verificationStatus +
                '}';
    }
}
