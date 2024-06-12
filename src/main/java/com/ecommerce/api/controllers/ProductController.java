package com.ecommerce.api.controllers;

import com.ecommerce.api.models.Product;
import com.ecommerce.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  @Autowired
  private ProductService productService;

  @PostMapping
  public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Product createdProduct = productService.createProduct(product.getName(), product.getPrice(), product.getDescription());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Product product = productService.getProduct(id);
    if (product != null) {
      return ResponseEntity.ok(product);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product, BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Product updatedProduct = productService.updateProduct(id, product);
    if (updatedProduct != null) {
      return ResponseEntity.ok(updatedProduct);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
