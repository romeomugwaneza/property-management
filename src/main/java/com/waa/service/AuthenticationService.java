package com.waa.service;

import com.waa.auth.*;
import com.waa.domain.User;
import com.waa.domain.Role;
import com.waa.config.JwtService;
import lombok.RequiredArgsConstructor;
import com.waa.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.waa.exceptions.UserNotFoundException;
import com.waa.exceptions.PasswordNotMatchException;
import com.waa.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService{
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) throws UserNotFoundException {
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("User not found.")
                );

        if (!user.isApproved()) throw new  UserNotFoundException("User not yet approved.");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest)
            throws UserAlreadyExistsException, PasswordNotMatchException {

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordCheck()))
            throw new PasswordNotMatchException("Password verification failed.");

        if (userRepository
                .findByEmail(registerRequest.getEmail())
                .isPresent()) throw new UserAlreadyExistsException("User with email: " + registerRequest.getEmail() + " already exists.");
        var role = Role.valueOf(registerRequest.getRole().toString());
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .isApproved(role != Role.OWNER)
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .message("success")
                .build();
    }

    @Override
    public ApproveResponse approve(int userId) throws UserNotFoundException{
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));

        user.setApproved(true);
        userRepository.save(user);

        return ApproveResponse.builder()
                .success(true)
                .message("Approved.")
                .build();
    }
}
