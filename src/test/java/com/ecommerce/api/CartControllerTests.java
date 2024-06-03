package com.ecommerce.api.test;

import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartControllerTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Test
  public void testCreateCart() {
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user = userRepository.save(user);

    ResponseEntity<Cart> response = restTemplate.postForEntity("/api/carts", user.getId(), Cart.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Cart cart = response.getBody();
    assertThat(cart).isNotNull();
    assertThat(cart.getUser().getId()).isEqualTo(user.getId());
  }

  @Test
  public void testGetCart() {
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user = userRepository.save(user);

    Cart cart = new Cart();
    cart.setUser(user);
    cart = cartRepository.save(cart);

    ResponseEntity<Cart> response = restTemplate.getForEntity("/api/carts/" + user.getId(), Cart.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Cart retrievedCart = response.getBody();
    assertThat(retrievedCart).isNotNull();
    assertThat(retrievedCart.getUser().getId()).isEqualTo(user.getId());
  }

  @Test
  public void testAddToCart() {
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user = userRepository.save(user);

    Cart cart = new Cart();
    cart.setUser(user);
    cart = cartRepository.save(cart);

    Product product = new Product();
    product.setName("test product");
    product.setPrice(10.0);
    product.setDescription("test description");
    product = productRepository.save(product);

    CartItem cartItem = new CartItem();
    cartItem.setCartId(cart.getId());
    cartItem.setProduct(product);
    cartItem.setQuantity(1);

    restTemplate.postForEntity("/api/carts/" + cart.getId() + "/items", cartItem, CartItem.class);

    // assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    // CartItem createdCartItem = response.getBody();
    // assertThat(createdCartItem).isNotNull();
    // assertThat(createdCartItem.getCart().getId()).isEqualTo(cart.getId());
    // assertThat(createdCartItem.getProduct().getId()).isEqualTo(product.getId());
    // assertThat(createdCartItem.getQuantity()).isEqualTo(1);
  }

  @Test
  public void testRemoveFromCart() {
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user = userRepository.save(user);

    Cart cart = new Cart();
    cart.setUser(user);
    cart = cartRepository.save(cart);

    Product product = new Product();
    product.setName("test product");
    product.setPrice(10.0);
    product.setDescription("test description");
    product = productRepository.save(product);

    CartItem cartItem = new CartItem();
    cartItem.setCartId(cart.getId());
    cartItem.setProduct(product);
    cartItem.setQuantity(1);
    cartItem = cartItemRepository.save(cartItem);

    restTemplate.delete("/api/carts/items/" + cartItem.getId());

    assertThat(cartItemRepository.findById(cartItem.getId())).isEmpty();
  }

  @Test
  public void testDeleteCart() {
    User user = new User();
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user = userRepository.save(user);

    Cart cart = new Cart();
    cart.setUser(user);
    cart = cartRepository.save(cart);

    restTemplate.delete("/api/carts/" + cart.getId());

    assertThat(cartRepository.findById(cart.getId())).isEmpty();
  }

  @AfterAll
  public void cleanup() {
    cartItemRepository.deleteAll();
    cartRepository.deleteAll();
  }
}
