package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakerycommonlib.exception.ExistingException;
import com.twochickendevs.bakerycommonlib.exception.NotFoundException;
import com.twochickendevs.bakeryuserservice.auth.model.Role;
import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import com.twochickendevs.bakeryuserservice.auth.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    public UserEntity signIn(UserDTO userDto) {
        validate(userDto);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new NotFoundException("can not find the user"));
    }

    public UserEntity signUp(UserDTO userDto) {
        validate(userDto);
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userDto.getUsername()))) {
            throw new ExistingException("User has exist");
        }

        UserEntity userEntity = UserEntity.builder().id(null).username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword())).firstName(userDto.getFirstName())
                .lastName(userDto.getLastName()).role(Role.USER).build();

        this.createTheAuthenticationInSecurityContext(userEntity);

        return userRepository.save(userEntity);
    }

    private void createTheAuthenticationInSecurityContext(UserEntity userEntity) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, null,
                userEntity.getAuthorities());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void validate(UserDTO userDto) {
        Objects.requireNonNull(userDto, "need to input the user's information");
        if (StringUtils.isAnyBlank(userDto.getUsername(), userDto.getPassword())) {
            throw new ValidationException("username and password can not be blank");
        }
    }
}
