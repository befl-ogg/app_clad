package com.spf.control.feature.user.service.impl;

import com.spf.control.feature.auth.domain.dto.request.AuthenticationRequest;
import com.spf.control.feature.user.domain.entity.User;
import com.spf.control.feature.user.domain.repository.UserRepository;
import com.spf.control.feature.user.service.UserAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticatorImpl implements UserAuthenticator {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public User authenticateUser(AuthenticationRequest request) {
        User user = userRepository.findByUserName(request.userName()).orElseThrow();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.password()));
        return user;
    }

}
