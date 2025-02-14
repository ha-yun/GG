package com.example.products.repository;

import com.example.products.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductsRepository extends JpaRepository<ProductEntity, Integer> {
    // 키워드 검색
    List<ProductEntity> findByPdtNameContaining(String keyword);

    // 가격 범위 필터링
    @Query("SELECT p FROM ProductEntity p WHERE p.pdtPrice BETWEEN :minPrice AND :maxPrice")
    List<ProductEntity> filterByPrice(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);
}