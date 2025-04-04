package com.spf.control.feature.user.service;

import com.spf.control.feature.auth.domain.dto.request.AuthenticationRequest;
import com.spf.control.feature.user.domain.entity.User;

public interface UserAuthenticator {
    User authenticateUser(AuthenticationRequest request);
}

