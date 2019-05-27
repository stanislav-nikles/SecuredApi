package com.hankuper.securedapi.controllers;

import com.hankuper.securedapi.dtos.ApiDto;
import com.hankuper.securedapi.dtos.JwtAuthenticationDto;
import com.hankuper.securedapi.dtos.LoginDto;
import com.hankuper.securedapi.dtos.SignUpDto;
import com.hankuper.securedapi.entities.Role;
import com.hankuper.securedapi.entities.RoleName;
import com.hankuper.securedapi.entities.User;
import com.hankuper.securedapi.repositories.RoleRepo;
import com.hankuper.securedapi.repositories.UserRepo;
import com.hankuper.securedapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationDto(jwtToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        if (userRepo.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity(new ApiDto(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepo.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity(new ApiDto(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(
                signUpDto.getName(),
                signUpDto.getUsername(),
                signUpDto.getEmail(),
                signUpDto.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepo.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepo.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiDto(true, "User registered successfully"));
    }
}
