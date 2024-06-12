package com.ecommerce.api.controllers;

import com.ecommerce.api.models.User;
import com.ecommerce.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private AuthenticationManager authenticationManager;
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @PostMapping("/api/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody User loginUser) {

    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginUser.getUsername(),
        loginUser.getPassword()
      )
    );
  
    // Set authentication in Security Context
    SecurityContextHolder.getContext().setAuthentication(authentication);
  
    // Generate JWT token
    String jwtToken = jwtTokenProvider.generateToken(authentication);
  
    return ResponseEntity.ok(jwtToken);
  }
}
