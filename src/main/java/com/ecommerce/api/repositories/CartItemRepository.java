package com.ecommerce.api.repositories;

import com.ecommerce.api.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}