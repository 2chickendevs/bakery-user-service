package com.twochickendevs.bakeryuserservice.auth.controller;

import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import com.twochickendevs.bakeryuserservice.auth.service.AuthenticationService;
import com.twochickendevs.bakeryuserservice.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final TokenService tokenService;

    @PostMapping("signin")
    public ResponseEntity<String> authenticateUser(@RequestBody UserDTO userDto) {
        final UserEntity userEntity = authenticationService.signIn(userDto);
        final String token = tokenService.create(userEntity);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO userDto) {

        final UserEntity userEntity = authenticationService.signUp(userDto);
        final String token = tokenService.create(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }
}
