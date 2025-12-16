package com.twochickendevs.bakeryuserservice.auth.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
  USER(Collections.emptyList()),

  ADMIN(
      Arrays.asList(
          Permission.ADMIN_CREATE,
          Permission.ADMIN_DELETE,
          Permission.ADMIN_READ,
          Permission.ADMIN_UPDATE));

  private final List<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities =
        getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
