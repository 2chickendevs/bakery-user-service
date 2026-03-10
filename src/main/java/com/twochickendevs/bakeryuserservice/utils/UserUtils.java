package com.twochickendevs.bakeryuserservice.utils;

import com.twochickendevs.bakerycommonlib.exception.NotFoundException;
import com.twochickendevs.bakeryuserservice.auth.model.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserUtils {

    public static String getCurrentUsername() {
        return UserUtils.getUserDetailsOpt().map(UserEntity::getUsername)
                .orElseThrow(() -> new NotFoundException("Can not find the username from userDetails"));
    }

    public static Long getCurrentUserId() {
        return UserUtils.getUserDetailsOpt().map(UserEntity::getId)
                .orElseThrow(() -> new NotFoundException("Can not get the user id from userDetails"));
    }

    public static Optional<UserEntity> getUserDetailsOpt() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(UserEntity.class::isInstance)
                .map(UserEntity.class::cast);
    }
}
