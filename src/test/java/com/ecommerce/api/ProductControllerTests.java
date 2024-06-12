package com.ecommerce.api.tests;

import com.ecommerce.api.models.Product;
import com.ecommerce.api.repositories.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.ecommerce.api.ApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void testCreateProduct_ValidProduct_ReturnsCreated() {
    // Prepare product data
    Product product = new Product();
    product.setName("testproduct");
    product.setPrice(100.0);
    product.setDescription("test description");

    // Perform POST request
    ResponseEntity<Product> response = restTemplate.postForEntity("/api/products", product, Product.class);

    // Verify response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("testproduct");
    assertThat(response.getBody().getPrice()).isEqualTo(100.0);
    assertThat(response.getBody().getDescription()).isEqualTo("test description");
  }

  /*
  @Test
  public void testGetAllProducts_ReturnsAllProducts() {
    // Perform GET request to retrieve all products
    ResponseEntity<List<Product>> response = restTemplate.exchange("/api/products", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Product>>() {});

    // Verify response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<Product> products = response.getBody();
    assertThat(products).isNotNull();
    assertThat(products.size()).isGreaterThan(0);
  } */

  @Test
  public void testGetProductById_ReturnsProductById() {
    Product productToCreate = new Product();
    productToCreate.setName("testproduct");
    productToCreate.setPrice(100.0);
    productToCreate.setDescription("test description");

    ResponseEntity<Product> createProductResponse = restTemplate.postForEntity("/api/products", productToCreate, Product.class);

    assertThat(createProductResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Product createdProduct = createProductResponse.getBody();
    assertThat(createdProduct).isNotNull();

    Long productId = createdProduct.getId();
    assertThat(productId).isNotNull();

    ResponseEntity<Product> getProductResponse = restTemplate.getForEntity("/api/products/" + productId, Product.class);

    assertThat(getProductResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Product retrievedProduct = getProductResponse.getBody();
    assertThat(retrievedProduct).isNotNull();
    assertThat(retrievedProduct.getId()).isEqualTo(productId);
  }

  @Test
  public void testUpdateProduct_ValidProduct_ReturnsUpdated() {
    Product productToCreate = new Product();
    productToCreate.setName("testproduct");
    productToCreate.setPrice(100.0);
    productToCreate.setDescription("test description");

    ResponseEntity<Product> createProductResponse = restTemplate.postForEntity("/api/products", productToCreate, Product.class);

    assertThat(createProductResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Product createdProduct = createProductResponse.getBody();
    assertThat(createdProduct).isNotNull();

    Long productId = createdProduct.getId();
    assertThat(productId).isNotNull();

    Product updatedProduct = new Product();
    updatedProduct.setName("updatedproduct");
    updatedProduct.setPrice(200.0);
    updatedProduct.setDescription("updated description");

    ResponseEntity<Product> updateProductResponse = restTemplate.exchange("/api/products/" + productId, HttpMethod.PUT, 
            new org.springframework.http.HttpEntity<>(updatedProduct), Product.class);

    assertThat(updateProductResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Product retrievedUpdatedProduct = updateProductResponse.getBody();
    assertThat(retrievedUpdatedProduct).isNotNull();
    assertThat(retrievedUpdatedProduct.getId()).isEqualTo(productId);
    assertThat(retrievedUpdatedProduct.getName()).isEqualTo("updatedproduct");
    assertThat(retrievedUpdatedProduct.getPrice()).isEqualTo(200.0);
    assertThat(retrievedUpdatedProduct.getDescription()).isEqualTo("updated description");
  }

  @Test
  public void testDeleteProduct_ReturnsNoContent() {
    Product productToCreate = new Product();
    productToCreate.setName("testproduct");
    productToCreate.setPrice(100.0);
    productToCreate.setDescription("test description");

    ResponseEntity<Product> createProductResponse = restTemplate.postForEntity("/api/products", productToCreate, Product.class);

    assertThat(createProductResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Product createdProduct = createProductResponse.getBody();
    assertThat(createdProduct).isNotNull();

    Long productId = createdProduct.getId();
    assertThat(productId).isNotNull();

    ResponseEntity<Void> deleteProductResponse = restTemplate.exchange("/api/products/" + productId, HttpMethod.DELETE, null, Void.class);

    assertThat(deleteProductResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @AfterAll
  public void cleanup() {
    productRepository.deleteAll();
  }
}
