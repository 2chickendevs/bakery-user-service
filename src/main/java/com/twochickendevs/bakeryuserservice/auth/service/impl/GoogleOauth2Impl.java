package com.twochickendevs.bakeryuserservice.auth.service.impl;

import com.twochickendevs.bakerycommonlib.exception.AuthenticationException;
import com.twochickendevs.bakerycommonlib.exception.InternalException;
import com.twochickendevs.bakeryuserservice.auth.model.*;
import com.twochickendevs.bakeryuserservice.auth.repository.UserRepository;
import com.twochickendevs.bakeryuserservice.auth.service.AbstractAuthenticationService;
import com.twochickendevs.bakeryuserservice.auth.service.IOauth2Service;
import com.twochickendevs.bakeryuserservice.auth.service.TokenService;
import com.twochickendevs.bakeryuserservice.client.GoogleTokenRetrieveClient;
import com.twochickendevs.bakeryuserservice.client.GoogleUserInfoClient;
import com.twochickendevs.bakeryuserservice.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

@Service
@Qualifier("googleOauth2Impl")
public class GoogleOauth2Impl extends AbstractAuthenticationService implements IOauth2Service {

    private static final String RESPONSE_TYPE = "code";
    private static final String INCLUDE_GRANTED_SCOPES = "true";

    @Value("${spring.security.oauth2.client.registration.google.url}")
    private String url;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUrl;

    private final GoogleTokenRetrieveClient googleTokenRetrieveClient;

    private final GoogleUserInfoClient googleUserInfoClient;

    private final CacheManager cacheManager;

    @Autowired
    public GoogleOauth2Impl(UserRepository userRepository,
                            PasswordEncoder encoder,
                            AuthenticationManager authenticationManager,
                            TokenService tokenService,
                            GoogleTokenRetrieveClient googleTokenRetrieveClient,
                            GoogleUserInfoClient googleUserInfoClient,
                            CacheManager cacheManager) {
        super(userRepository, encoder, authenticationManager, tokenService);
        this.googleTokenRetrieveClient = googleTokenRetrieveClient;
        this.googleUserInfoClient = googleUserInfoClient;
        this.cacheManager = cacheManager;
    }

    @Override
    public String buildAuthUrl() {
        String state = UUID.randomUUID().toString();
        Cache cache = cacheManager.getCache(CacheKey.GOOGLE_STATE);
        if (cache == null) {
            throw new InternalException("[BuildAuthUrl] The google state cache not found");
        }

        cache.put(state, state);

        return UriComponentsBuilder.fromPath(url)
                .queryParam("scope", scope)
                .queryParam("include_granted_scopes", INCLUDE_GRANTED_SCOPES)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUrl)
                .queryParam("client_id", clientId)
                .toUriString();
    }

    @Override
    public String getAccessToken(String code, String state) {
        Cache cache = cacheManager.getCache(CacheKey.GOOGLE_STATE);
        if (cache == null) {
            throw new InternalException("[GoogleGetAccessToken] The google state cache not found");
        }
        if(cache.get(state) == null) {
            throw new AuthenticationException("[GoogleGetAccessToken] Not found state " + state);
        }

        GoogleToken googleToken = googleTokenRetrieveClient.exchangeCodeForToken(
                GoogleTokenRequest.builder()
                        .code(code)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .redirectUri(redirectUrl)
                        .grantType(Constant.AUTHORIZATION_CODE)
                        .build()
        );

        GoogleUserInfo googleUserInfo = googleUserInfoClient.getUserInfo(
                Constant.BEARER + googleToken.getAccessToken());

        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(googleUserInfo.getEmail());
        if (userEntityOpt.isEmpty()) {
            return signUp(googleUserInfo);
        }

        UserEntity userEntity = userEntityOpt.get();
        String storedGoogleId = userEntity.getGoogleId();
        String newGoogleId = googleUserInfo.getId();

        createTheAuthenticationInSecurityContext(userEntity);

        if (newGoogleId == null || (
                StringUtils.isNotBlank(storedGoogleId) &&
                !storedGoogleId.equals(googleUserInfo.getId()))
        ) {
            throw new IllegalArgumentException("[GoogleLogin] The google id not match");
        }

        if (StringUtils.isBlank(storedGoogleId)) {
            updateGoogleId(userEntity, googleUserInfo.getId());
        }

        return createToken(userEntity);
    }

    private void updateGoogleId(UserEntity userEntity, String googleId) {
        userEntity.setGoogleId(googleId);
        userRepository.save(userEntity);
    }

    private String signUp(GoogleUserInfo googleUserInfo) {
        UserDTO userDTO = UserDTO.builder()
                .firstName(googleUserInfo.getFirstName())
                .lastName(googleUserInfo.getLastName())
                .username(googleUserInfo.getEmail())
                .googleId(googleUserInfo.getId())
                .build();

        UserEntity userEntity = createUser(userDTO);

        return createToken(userEntity);
    }
}
