package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakeryuserservice.auth.model.UserDTO;

public interface IOauth2Service {

    String buildAuthUrl();

    String getAccessToken(String code, String state);
}