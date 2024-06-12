package com.ecommerce.api.repositories;

import com.ecommerce.api.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);
    List<Roles> findByNameIn(List<String> names);
}
