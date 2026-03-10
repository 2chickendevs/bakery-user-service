package com.twochickendevs.bakeryuserservice.auth.service;

public interface IOauth2Service {

    String buildAuthUrl();

    String getAccessToken(String code, String state);
}