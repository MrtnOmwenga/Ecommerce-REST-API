package com.ecommerce.api.services;

import com.ecommerce.api.models.Cart;
import com.ecommerce.api.models.CartItem;
import com.ecommerce.api.models.Product;
import com.ecommerce.api.models.User;
import com.ecommerce.api.repositories.CartItemRepository;
import com.ecommerce.api.repositories.CartRepository;
import com.ecommerce.api.repositories.ProductRepository;
import com.ecommerce.api.repositories.UserRepository;
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
