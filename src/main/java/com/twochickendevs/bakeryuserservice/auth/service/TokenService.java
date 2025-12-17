package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakeryuserservice.auth.model.TokenEntity;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import com.twochickendevs.bakeryuserservice.auth.repository.TokenRepository;
import com.twochickendevs.bakeryuserservice.auth.utils.JwtUtils;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private final JwtUtils jwtUtils;

    public String create(UserEntity userEntity) {
        if (userEntity.getId() == null || StringUtils.isBlank(userEntity.getUsername())) {
            throw new ValidationException("Can not specify the user");
        }
        this.removeAllByUser(userEntity.getId());
        final String token = this.initToken(userEntity);
        this.storeToken(token, userEntity);
        return token;
    }

    private void removeAllByUser(Long userId) {
        try {
            tokenRepository.removeAllTokenByUserId(userId);
        } catch (Exception e) {
            log.error("Can not remove all token with user [{}]", userId, e);
        }
    }

    private String initToken(UserEntity userEntity) {
        return jwtUtils.generateJwtToken(userEntity.getUsername());
    }

    private void storeToken(String token, UserEntity userEntity) {
        final TokenEntity tokenEntity = TokenEntity.builder().token(token).user(userEntity).build();
        tokenRepository.save(tokenEntity);
    }
}
