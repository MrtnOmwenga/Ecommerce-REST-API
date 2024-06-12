package com.ecommerce.api.controllers;

import com.ecommerce.api.models.Cart;
import com.ecommerce.api.models.CartItem;
import com.ecommerce.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/carts")
public class CartController {

  @Autowired
  private CartService cartService;

  @PostMapping
  public ResponseEntity<Cart> createCart(@Valid @RequestBody Long userId, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Cart createdCart = cartService.createCart(userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
    Cart cart = cartService.getCart(userId);
    if (cart == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(cart);
  }

  @PostMapping("/{cartId}/items")
  public ResponseEntity<CartItem> addToCart(@PathVariable Long cartId, @Valid @RequestBody CartItem cartItem, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    CartItem addedItem = cartService.addToCart(cartId, cartItem.getProduct().getId(), cartItem.getQuantity());
    return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
  }

  @DeleteMapping("/items/{itemId}")
  public ResponseEntity<Void> removeFromCart(@PathVariable Long itemId) {
    boolean removed = cartService.removeFromCart(itemId);
    if (!removed) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{cartId}")
  public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
    boolean deleted = cartService.deleteCart(cartId);
    if (!deleted) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
