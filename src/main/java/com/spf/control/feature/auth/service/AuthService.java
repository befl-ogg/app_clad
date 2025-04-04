package com.spf.control.feature.auth.service;

import com.spf.control.feature.auth.domain.dto.request.AuthenticationRequest;
import com.spf.control.feature.auth.domain.dto.request.UserRequest;
import com.spf.control.feature.auth.domain.dto.response.AuthenticationResponse;
import com.spf.control.feature.auth.domain.dto.response.RegisterResponse;

public interface AuthService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    RegisterResponse register(UserRequest request);
}
