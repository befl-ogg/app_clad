package com.spf.control.infrastructure.handler;

import com.spf.control.feature.user.domain.repository.UserRepository;
import com.spf.control.infrastructure.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationEventPublisher {

    final LoginAttemptService loginAttemptService;
    final UserRepository userRepository;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        loginAttemptService.unblockUser(userDetail.getUsername());
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        log.error("Error in login: {}", exception.getMessage());
        loginAttemptService.loginFailed(authentication.getName());
        log.info("Authentication attempt for username: {}", authentication.getName());
    }
}
