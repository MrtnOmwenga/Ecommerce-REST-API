package com.ecommerce.api.controller;

import com.ecommerce.api.model.User;
import com.ecommerce.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<User> createUser(@Valid @RequestBody User user, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    User createdUser = userService.createUser(user.getUsername(), user.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.getUser(id);
    System.out.println(user);
    if (user != null) {
      return ResponseEntity.ok(user);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    User updatedUser = userService.updateUser(id, user);
    if (updatedUser != null) {
      return ResponseEntity.ok(updatedUser);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
