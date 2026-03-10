package com.twochickendevs.bakeryuserservice.auth.controller;

import com.twochickendevs.bakeryuserservice.auth.mapper.UserMapper;
import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;
import com.twochickendevs.bakeryuserservice.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUserInformation() {
        UserDTO userDto = UserMapper.INSTANCE.toDTO(userService.getCurrent());
        return ResponseEntity.ok(userDto);
    }
}
