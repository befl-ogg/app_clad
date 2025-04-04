package com.spf.control.infrastructure.filter;

import com.spf.control.feature.token.domain.repository.TokenRepository;
import com.spf.control.infrastructure.config.JwtTokenUtil;
import com.spf.control.infrastructure.web.request.CustomHttpServletRequest;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    @Autowired(required=false)
    private JwtTokenUtil jwtTokenUtil;
    @Autowired(required=false)
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());
        String jwt = jwtTokenUtil.parseJwt(request);
        log.info("jwt: " + jwt);
        String username = "";
        Long idUser = 0L;
        CustomHttpServletRequest requestCustom= new CustomHttpServletRequest(request);

        try {
            username = jwtTokenUtil.getUsernameFromToken(jwt);
            idUser = jwtTokenUtil.getIdUserFromToken(jwt);
            log.info("username: " + username);
            log.info("ID_USER: " + idUser);
        } catch(JwtException e) {
            log.error(e.getMessage());
        }

        if (!ObjectUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            var isTokenValid = this.tokenRepository.findByToken(jwt)
                    .map(t -> !t.getExpiredAccessToken())
                    .orElse(false);
            log.info("isTokenValid: " + isTokenValid);


            if (jwtTokenUtil.validateToken(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                requestCustom.addHeader("userId", idUser.toString());
            }

        }

        filterChain.doFilter(requestCustom, response);

    }
}
