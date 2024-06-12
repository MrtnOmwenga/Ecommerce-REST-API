package com.ecommerce.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "app_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Roles {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String name;

  // Default constructor
  public Roles() {}

  public Roles(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}