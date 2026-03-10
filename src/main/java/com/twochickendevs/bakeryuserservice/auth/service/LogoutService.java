package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakeryuserservice.auth.repository.TokenRepository;
import com.twochickendevs.bakeryuserservice.constant.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(Constant.BEARER)) {
            return;
        }

        final String jwt = authHeader.substring(7);

        tokenRepository.findByToken(jwt).ifPresent(tokenRepository::delete);
    }
}
