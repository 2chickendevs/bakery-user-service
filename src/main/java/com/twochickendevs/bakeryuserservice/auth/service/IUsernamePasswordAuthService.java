package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;

public interface IUsernamePasswordAuthService {

    String signIn(UserDTO userDto);

    String signUp(UserDTO userDto);
}
