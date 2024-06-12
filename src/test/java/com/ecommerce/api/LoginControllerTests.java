package com.ecommerce.api.tests;

import com.ecommerce.api.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.ecommerce.api.ApiApplication.class)
public class LoginControllerTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testAuthenticateUser_ValidCredentials_ReturnsJwtToken() {
    // Prepare user data
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("password123");

    // Perform POST request to authenticate user
    ResponseEntity<String> response = restTemplate.postForEntity("/api/login", user, String.class);

    // Verify response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).startsWith("Bearer ");
  }

  @Test
  public void testAuthenticateUser_InvalidCredentials_ReturnsUnauthorized() {
    // Prepare user data with invalid credentials
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("invalidPassword");

    // Perform POST request to authenticate user with invalid credentials
    ResponseEntity<String> response = restTemplate.postForEntity("/api/login", user, String.class);

    // Verify response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }
}
