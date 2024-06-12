package com.ecommerce.api.services;

import com.ecommerce.api.models.User;
import com.ecommerce.api.models.Roles;
import com.ecommerce.api.repositories.UserRepository;
import com.ecommerce.api.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RolesRepository rolesRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User createUser(String username, String email, String password, List<String> roleNames) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("Username already exists");
    }

    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));

    List<Roles> roles = rolesRepository.findByNameIn(roleNames);
    user.setRoles(roles);
    
    return userRepository.save(user);
  }

  public User getUser(Long userId) {
    return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  public User updateUser(Long userId, User updatedUser) {
    User existingUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
      userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists");
    }

    if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
      userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already exists");
    }

    existingUser.setUsername(updatedUser.getUsername());
    existingUser.setEmail(updatedUser.getEmail());

    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
      existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
    }

    return userRepository.save(existingUser);
  }

  public void deleteUser(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new IllegalArgumentException("User not found");
    }
    userRepository.deleteById(userId);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Load user from the database based on username
    User user = userRepository.findByUsername(username)
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

    // Return a UserDetails object for the authenticated user
    return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(mapRolesToAuthorities(user.getRoles()))
            .build();
  }

  private Collection<GrantedAuthority> mapRolesToAuthorities(List<Roles> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
  }
}