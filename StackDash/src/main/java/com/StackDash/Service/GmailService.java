package com.StackDash.Service;

import com.StackDash.Entity.User;

public interface GmailService {
    public void sendOtpToUser(User user, String otp);
}
