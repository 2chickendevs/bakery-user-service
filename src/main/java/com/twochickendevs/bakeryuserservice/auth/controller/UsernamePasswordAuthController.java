package com.twochickendevs.bakeryuserservice.auth.controller;

import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.service.IUsernamePasswordAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class UsernamePasswordAuthController {

    private final IUsernamePasswordAuthService usernamePasswordAuthService;

    @Autowired
    public UsernamePasswordAuthController(
            @Qualifier("usernamePasswordAuthServiceImpl")
            IUsernamePasswordAuthService usernamePasswordAuthService
    ) {
        this.usernamePasswordAuthService = usernamePasswordAuthService;
    }

    @PostMapping("signin")
    public ResponseEntity<String> authenticateUser(@RequestBody UserDTO userDto) {
        return ResponseEntity.ok(usernamePasswordAuthService.signIn(userDto));
    }

    @PostMapping(value = "signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserDTO userDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usernamePasswordAuthService.signUp(userDto));
    }
}
