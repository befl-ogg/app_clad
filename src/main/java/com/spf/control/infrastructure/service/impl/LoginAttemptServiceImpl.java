package com.spf.control.infrastructure.service.impl;

import com.spf.control.feature.user.domain.repository.UserRepository;
import com.spf.control.infrastructure.service.LoginAttemptService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final UserRepository userRepository;
    private final ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    public static final int MAX_ATTEMPTS = 5;
    public static final int BLOCK_TIME_MINUTES = 15;


    @Override
    public void loginFailed(String userName) {
        int attempts = loginAttempts.getOrDefault(userName, 0);
        attempts++;
        loginAttempts.put(userName, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            blockUser(userName);
        }
    }

    @Override
    public int getAttempts(String userName) {
        return loginAttempts.getOrDefault(userName, 0);
    }

    private void blockUser(String userName) {
        var user  = userRepository.findByUserName(userName)
                .orElseThrow(EntityNotFoundException::new);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(String userName) {
        userRepository.findByUserName(userName).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
            loginAttempts.remove(userName);
        });
        loginAttempts.remove(userName);
    }

}
