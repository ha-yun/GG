package com.example.products.repository;

import com.example.products.entity.CustomGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomGoodsRepository extends JpaRepository<CustomGoods, Integer> {
}
