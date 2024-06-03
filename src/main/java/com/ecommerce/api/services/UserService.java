package com.ecommerce.api.service;

import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User createUser(String username, String email) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    return userRepository.save(user);
  }

  public User getUser(Long userId) {
    return userRepository.findById(userId).orElse(null);
  }

  public User updateUser(Long userId, User updatedUser) {
    User existingUser = userRepository.findById(userId).orElse(null);
    if (existingUser != null) {
      existingUser.setUsername(updatedUser.getUsername());
      existingUser.setEmail(updatedUser.getEmail());
      return userRepository.save(existingUser);
    }
    return null;
  }

  public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
  }
}