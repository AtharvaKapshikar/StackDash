package com.StackDash.Entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "StackDash_Otp_Verify")
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "otp_seq")
    @SequenceGenerator(name = "otp_seq", sequenceName = "stack_dash_otp_seq", allocationSize = 1)
    private Long id;

    private String email;           // or phone number
    private String otpCode;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean isVerified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "OtpEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", otpCode='" + otpCode + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", isVerified=" + isVerified +
                '}';
    }
}
