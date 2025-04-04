package com.spf.control.feature.auth.web;

import com.spf.control.feature.auth.domain.dto.request.AuthenticationRequest;
import com.spf.control.feature.auth.domain.dto.request.UserRequest;
import com.spf.control.feature.auth.domain.dto.response.AuthenticationResponse;
import com.spf.control.feature.auth.domain.dto.response.RegisterResponse;
import com.spf.control.feature.auth.service.AuthService;
import com.spf.control.feature.token.application.factory.TokenResponseFactory;
import com.spf.control.feature.user.mapper.UserMapper;
import com.spf.control.feature.user.service.UserAuthenticator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserAuthenticator userAuthenticator;
    private final TokenResponseFactory tokenResponseFactory;


    @PostMapping
    AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest request) {
        authService.authenticate(request);
        var user = userAuthenticator.authenticateUser(request);
        var token = tokenResponseFactory.createAndRevokePreviousTokens(user);
        return UserMapper.INSTANCE.toDTO(user, token.token(), token.refreshToken());
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody UserRequest request) {
        return this.authService.register(request);
    }

}
