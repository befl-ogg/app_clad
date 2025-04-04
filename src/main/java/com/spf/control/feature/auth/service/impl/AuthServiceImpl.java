package com.spf.control.feature.auth.service.impl;

import com.spf.control.feature.auth.domain.dto.request.AuthenticationRequest;
import com.spf.control.feature.auth.domain.dto.request.UserRequest;
import com.spf.control.feature.auth.domain.dto.response.AuthenticationResponse;
import com.spf.control.feature.auth.domain.dto.response.RegisterResponse;
import com.spf.control.feature.auth.service.AuthService;
import com.spf.control.feature.common.exception.StudioNotFoundException;
import com.spf.control.feature.common.exception.UserAlreadyExistsException;
import com.spf.control.feature.studio.domain.repository.StudioRepository;
import com.spf.control.feature.token.application.factory.TokenResponseFactory;
import com.spf.control.feature.token.domain.repository.TokenRepository;
import com.spf.control.feature.user.domain.entity.User;
import com.spf.control.feature.user.domain.enums.RoleType;
import com.spf.control.feature.user.domain.repository.UserRepository;
import com.spf.control.feature.user.mapper.UserMapper;
import com.spf.control.feature.user.mapper.UserStudioMapper;
import com.spf.control.feature.user.service.UserAuthenticator;
import com.spf.control.feature.userstudio.domain.entity.UserStudio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final StudioRepository studioRepository;
    private final TokenResponseFactory tokenResponseFactory;
    private final UserAuthenticator userAuthenticator;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userAuthenticator.authenticateUser(request);
        var token = tokenResponseFactory.createAndRevokePreviousTokens(user);
        return UserMapper.INSTANCE.toDTO(user, token.token(), token.refreshToken());
    }

    @Transactional
    @Override
    public RegisterResponse register(UserRequest request) {
        var studio = studioRepository.findByStudioCode(request.getStudioCode()).orElseThrow(() -> new StudioNotFoundException(request.getStudioCode()));
        userRepository.findByUserNameOrEmail(request.getUserName(), request.getEmail()).ifPresent(
                user -> { throw new UserAlreadyExistsException(request.getUserName()); }
        );
        var user = UserMapper.INSTANCE.toEntity(request,
                passwordEncoder.encode(request.getPassword()),
                Boolean.TRUE,
                RoleType.valueOf(request.getRoleType()));

        UserStudio userStudio = UserStudioMapper.INSTANCE.toEntity(user, studio, true);

        user.setUserStudios(List.of(userStudio));
        var savedUser = userRepository.save(user);
        System.out.println(savedUser.toString());

        return UserMapper.INSTANCE.toDTO(savedUser);
    }
}

