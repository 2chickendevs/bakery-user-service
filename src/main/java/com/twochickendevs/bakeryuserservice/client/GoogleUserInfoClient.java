package com.twochickendevs.bakeryuserservice.client;

import com.twochickendevs.bakeryuserservice.auth.model.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleUserInfo", url = "${app.clients.googleUserInfo.url}")
public interface GoogleUserInfoClient {

    @GetMapping("/oauth2/v2/userinfo")
    GoogleUserInfo getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);
}
