package com.example.products.config;

import com.example.products.entity.ProductEntity;
import com.example.products.repository.ProductsRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 데이터 임의로 넣기
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductsRepository productsRepository;

    @PostConstruct
    public void initData() {
        if (productsRepository.count() == 0) { // 중복 삽입 방지
            List<ProductEntity> products = List.of(
                    createProduct("소녀시대 포토카드 세트", 15000, 100),
                    createProduct("소녀시대 공식 응원봉", 32000, 50),
                    createProduct("소녀시대 티셔츠", 28000, 70),
                    createProduct("소녀시대 한정판 앨범", 45000, 30),
                    createProduct("소녀시대 4CUT PHOTO SET", 8000, 100)
            );
            productsRepository.saveAll(products);
        }
    }

    private ProductEntity createProduct(String name, int price, int quantity) {
        ProductEntity product = new ProductEntity();
        product.setPdtName(name);
        product.setPdtPrice(price);
        product.setPdtQuantity(quantity);
        return product;
    }
}

