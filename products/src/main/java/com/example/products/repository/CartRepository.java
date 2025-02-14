package com.example.products.repository;

import com.example.products.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    List<CartEntity> findByEmail(String email);
    Optional<CartEntity> findByEmailAndPdtId(String email, Integer pdtId);
}
