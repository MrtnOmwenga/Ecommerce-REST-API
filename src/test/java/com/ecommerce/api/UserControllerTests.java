package com.ecommerce.api.tests;

import com.ecommerce.api.models.User;
import com.ecommerce.api.repositories.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.AfterAll;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.ecommerce.api.ApiApplication.class)
public class UserControllerTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testCreateUser_ValidUser_ReturnsCreated() {
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.setPassword("password123");

    ResponseEntity<User> response = restTemplate.postForEntity("/api/users", user, User.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getUsername()).isEqualTo("testuser");
    assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");
    assertThat(response.getBody().getPassword()).isNull();
  }

  /*
  @Test
  public void testGetAllUsers_ReturnsAllUsers() {
    // Perform GET request to retrieve all users
    ResponseEntity<List<User>> response = restTemplate.exchange("/api/users", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<User>>() {});

    // Verify response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    System.out.println("==================================================================================");
    System.out.println(response.getBody());
    System.out.println("==================================================================================");
    
    List<User> users = response.getBody();
    assertThat(users).isNotNull();
    assertThat(users.size()).isGreaterThan(0);
  } */

  @Test
  public void testGetUserById_ReturnsUserById() {
    User userToCreate = new User();
    userToCreate.setUsername("testuser");
    userToCreate.setEmail("test@example.com");
    userToCreate.setPassword("password123");

    ResponseEntity<User> createUserResponse = restTemplate.postForEntity("/api/users", userToCreate, User.class);

    assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    User createdUser = createUserResponse.getBody();
    assertThat(createdUser).isNotNull();

    Long userId = createdUser.getId();
    assertThat(userId).isNotNull();

    ResponseEntity<User> getUserResponse = restTemplate.getForEntity(String.format("/api/users/%d", userId), User.class);

    assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    User retrievedUser = getUserResponse.getBody();
    assertThat(retrievedUser).isNotNull();
    assertThat(retrievedUser.getId()).isEqualTo(userId);
  }

  @Test
  public void testUpdateUser_ValidUser_ReturnsUpdated() {
    User userToCreate = new User();
    userToCreate.setUsername("testuser2");
    userToCreate.setEmail("test2@example.com");
    userToCreate.setPassword("password123");

    ResponseEntity<User> createUserResponse = restTemplate.postForEntity("/api/users", userToCreate, User.class);

    assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    User createdUser = createUserResponse.getBody();
    assertThat(createdUser).isNotNull();

    Long userId = createdUser.getId();
    assertThat(userId).isNotNull();

    // Update user
    User userToUpdate = new User();
    userToUpdate.setUsername("updateduser");
    userToUpdate.setEmail("updated@example.com");

    ResponseEntity<User> updateUserResponse = restTemplate.exchange(String.format("/api/users/%d", userId), HttpMethod.PUT, new org.springframework.http.HttpEntity<>(userToUpdate), User.class);

    // Verify update response
    assertThat(updateUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    User updatedUser = updateUserResponse.getBody();
    assertThat(updatedUser).isNotNull();
    assertThat(updatedUser.getId()).isEqualTo(userId);
    assertThat(updatedUser.getUsername()).isEqualTo("updateduser");
    assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
  }

  @Test
  public void testDeleteUser_ReturnsNoContent() {
    User userToCreate = new User();
    userToCreate.setUsername("testuser3");
    userToCreate.setEmail("test3@example.com");
    userToCreate.setPassword("password123");

    ResponseEntity<User> createUserResponse = restTemplate.postForEntity("/api/users", userToCreate, User.class);

    assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    User createdUser = createUserResponse.getBody();
    assertThat(createdUser).isNotNull();

    Long userId = createdUser.getId();
    assertThat(userId).isNotNull();

    ResponseEntity<Void> deleteUserResponse = restTemplate.exchange("/api/users/" + userId, HttpMethod.DELETE, null, Void.class);

    assertThat(deleteUserResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<User> getUserResponse = restTemplate.getForEntity("/api/users/" + userId, User.class);
    assertThat(getUserResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCreateUser_DuplicateUsername_ReturnsBadRequest() {
    // Assume a user with the same username already exists in the repository
    User existingUser = new User();
    existingUser.setUsername("existinguser");
    existingUser.setEmail("existing@example.com");
    existingUser.setPassword("password123");
    userRepository.save(existingUser);

    // Try to create a new user with the same username
    User newUser = new User();
    newUser.setUsername("existinguser");
    newUser.setEmail("new@example.com");
    newUser.setPassword("password123");

    ResponseEntity<Void> response = restTemplate.postForEntity("/api/users", newUser, Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @AfterAll
  public void cleanup() {
    userRepository.deleteAll();
  }
}
