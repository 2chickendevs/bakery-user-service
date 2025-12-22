package com.twochickendevs.bakeryuserservice.auth.service.impl;

import com.twochickendevs.bakerycommonlib.exception.ExistingException;
import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import com.twochickendevs.bakeryuserservice.auth.repository.UserRepository;
import com.twochickendevs.bakeryuserservice.auth.service.AbstractAuthenticationService;
import com.twochickendevs.bakeryuserservice.auth.service.IUsernamePasswordAuthService;
import com.twochickendevs.bakeryuserservice.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("usernamePasswordAuthServiceImpl")
public class UsernamePasswordAuthServiceImpl extends AbstractAuthenticationService implements IUsernamePasswordAuthService {

    @Autowired
    public UsernamePasswordAuthServiceImpl(UserRepository userRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        super(userRepository, encoder, authenticationManager, tokenService);
    }

    public String signIn(UserDTO userDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        return createToken(userEntity);
    }

    public String signUp(UserDTO userDto) {
        if (isUserPresent(userDto.getUsername())) {
            throw new ExistingException("User exists with user name " + userDto.getUsername());
        }

        UserEntity userEntity = createUser(userDto);

        return createToken(userEntity);
    }
}
