package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakeryuserservice.auth.model.Role;
import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import com.twochickendevs.bakeryuserservice.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public abstract class AbstractAuthenticationService {

    protected final UserRepository userRepository;

    protected final PasswordEncoder encoder;

    protected final AuthenticationManager authenticationManager;

    protected final TokenService tokenService;

    protected boolean isUserPresent(String username) {
        return userRepository.existsByUsername(username);
    }

    protected UserEntity createUser(UserDTO userDto) {
        UserEntity userEntity = UserEntity.builder()
                .id(null).username(userDto.getUsername())
                .password(StringUtils.isNoneBlank(userDto.getPassword()) ? encoder.encode(userDto.getPassword()) : null)
                .firstName(userDto.getFirstName()).lastName(userDto.getLastName())
                .role(Role.USER).build();

        this.createTheAuthenticationInSecurityContext(userEntity);

        return userRepository.save(userEntity);
    }

    protected String createToken(UserEntity userEntity) {
        return tokenService.create(userEntity);
    }

    /**
     * Add user to context, so when add or update database Spring boot can get the user.
     * */
    private void createTheAuthenticationInSecurityContext(UserEntity userEntity) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, null,
                userEntity.getAuthorities());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
