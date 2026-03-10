package com.twochickendevs.bakeryuserservice.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank
    @Email
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String googleId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
