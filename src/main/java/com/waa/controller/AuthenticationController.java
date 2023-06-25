package com.waa.controller;

import com.waa.auth.*;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.waa.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import com.waa.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.waa.exceptions.PasswordNotMatchException;
import com.waa.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/test")
    public String test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Iterate over the authorities to retrieve the role(s)
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                // ... Process the role as needed
                System.out.println("User role: " + role);
            }

            if (principal instanceof UserDetails userDetails) {

                String username = userDetails.getUsername();

                System.out.println("Currently logged-in user: " + userDetails);
            }
        }


        return "Public test endpoint working! :)";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        try {
            return ResponseEntity.ok(authenticationService.register(registerRequest));
        } catch (UserAlreadyExistsException | PasswordNotMatchException e) {
            var response = AuthErrorResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginRequest authenticationRequest
    ) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        } catch (UserNotFoundException e) {
            var response = AuthErrorResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable("id") Integer userId) {
        try {
            var response = authenticationService.approve(userId);
            return ResponseEntity.ok().body(response);
        } catch (UserNotFoundException e) {
            var response = ApproveResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(response);
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "Logout ...";
    }
}
