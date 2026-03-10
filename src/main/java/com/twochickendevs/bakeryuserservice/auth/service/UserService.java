package com.twochickendevs.bakeryuserservice.auth.service;

import com.twochickendevs.bakerycommonlib.exception.NotFoundException;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import com.twochickendevs.bakeryuserservice.auth.repository.UserRepository;
import com.twochickendevs.bakeryuserservice.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getCurrent() {
        return UserUtils.getUserDetailsOpt().orElseThrow(() -> new NotFoundException("can not find user entity"));
    }

    public UserEntity findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("can not find user entity with username: " + username));
    }
}
