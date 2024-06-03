package com.ecommerce.api.service;

import com.ecommerce.api.model.Cart;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.CartRepository;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  public Cart createCart(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    Cart cart = new Cart();
    cart.setUser(user);
    return cartRepository.save(cart);
  }

  public Cart getCart(Long userId) {
    return cartRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
  }

  public CartItem addToCart(Long cartId, Long productId, int quantity) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

    CartItem cartItem = new CartItem();
    cartItem.setCartId(cartId);
    cartItem.setProduct(product);
    cartItem.setQuantity(quantity);

    return cartItemRepository.save(cartItem);
  }

  public boolean removeFromCart(Long itemId) {
    if (cartItemRepository.existsById(itemId)) {
      cartItemRepository.deleteById(itemId);
      return true;
    }
    return false;
  }

  public boolean deleteCart(Long cartId) {
    if (cartRepository.existsById(cartId)) {
      cartRepository.deleteById(cartId);
      return true;
    }
    return false;
  }
}
