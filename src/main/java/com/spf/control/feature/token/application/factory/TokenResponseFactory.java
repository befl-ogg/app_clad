package com.spf.control.feature.token.application.factory;

import com.spf.control.feature.token.application.dto.TokenResponse;
import com.spf.control.feature.token.domain.entity.Token;
import com.spf.control.feature.token.domain.enums.TokenType;
import com.spf.control.feature.token.domain.repository.TokenRepository;
import com.spf.control.feature.user.domain.entity.User;
import com.spf.control.infrastructure.config.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class TokenResponseFactory {
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenRepository tokenRepository;


    public TokenResponse create(User user) {
        return new TokenResponse(jwtTokenUtil.generateToken(user), jwtTokenUtil.generateRefreshToken());
    }

    /**
     * Crea una nueva respuesta de token para el usuario dado.
     * <p>
     * Revoca todos los tokens anteriores del usuario y guarda el nuevo token en la base de datos.
     *
     * @param user El usuario para el cual se está creando la respuesta de token.
     * @return La nueva respuesta de token generada.
     */


    public TokenResponse createAndRevokePreviousTokens(User user) {
        var token = new TokenResponse(jwtTokenUtil.generateToken(user), jwtTokenUtil.generateRefreshToken());
        revokePreviousTokens(user);
        saveUserToken(user, token);
        return token;
    }

    public void closeSession(Long userId) {
        var tokens = tokenRepository.findAllValidTokenByUser(userId);
        var tokenIds = tokens.stream().map(Token::getId).toList();
        tokenRepository.deleteAllByIdInBatch(tokenIds);
    }

    private void saveUserToken(User user, TokenResponse tokenResponse) {
        tokenRepository.save(Token.builder()
                .user(user)
                .token(tokenResponse.token())
                .refreshToken(tokenResponse.refreshToken())
                .tokenType(TokenType.BEARER)
                .expiredAccessToken(false)
                .expiredRefreshToken(false)
                .createdDate(new Date())
                .build());
    }

    private void revokePreviousTokens(User user) {
        var tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        tokens.forEach(token -> {
            token.setExpiredAccessToken(true);
            token.setExpiredRefreshToken(TRUE);
            token.setExpiredDate(new Date());
            tokenRepository.save(token);
        });
    }
}
