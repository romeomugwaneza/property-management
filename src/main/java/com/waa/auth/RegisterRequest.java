package com.waa.auth;

import lombok.Data;
import lombok.Builder;
import com.waa.domain.Role;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordCheck;
    private Role role;
}
