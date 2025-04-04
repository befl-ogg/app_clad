package com.spf.control.infrastructure.service;

public interface LoginAttemptService {
    void loginFailed(String userName);
    int getAttempts(String userName);
    void unblockUser(String userName);
}
