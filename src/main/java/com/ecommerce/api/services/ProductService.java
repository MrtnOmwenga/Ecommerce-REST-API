package com.ecommerce.api.service;

import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  public Product createProduct(String name, double price, String description) {
    Product product = new Product();
    product.setName(name);
    product.setPrice(price);
    product.setDescription(description);
    return productRepository.save(product);
  }

  public Product getProduct(Long id) {
    return productRepository.findById(id).orElse(null);
  }

  public Product updateProduct(Long id, Product product) {
    if (productRepository.existsById(id)) {
      product.setId(id);
      return productRepository.save(product);
    } else {
      return null;
    }
  }

  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}
