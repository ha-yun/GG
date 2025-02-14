package com.example.products.service;

import com.example.products.dto.ProductDetailDto;
import com.example.products.dto.ProductDto;
import com.example.products.dto.ShoppingCartReqDto;
import com.example.products.entity.CartEntity;
import com.example.products.entity.ProductEntity;
import com.example.products.kafka.KafkaProducer;
import com.example.products.repository.CartRepository;
import com.example.products.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private KafkaProducer kafkaProducer;


    public List<ProductDto> allProducts() {
        List<ProductEntity> pdts = productsRepository.findAll();
        return pdts.stream()
                .map(p-> ProductDto.builder()
                        .pdtName(p.getPdtName())
                        .pdtPrice(p.getPdtPrice())
                        .build())
                .collect(Collectors.toList());
    }

    public ProductDetailDto getProductDetailInfo(Integer pdtId) {
        ProductEntity productEntity = productsRepository.findById(pdtId)
                .orElseThrow(()->new IllegalArgumentException("Product not found or pdtId miss"));

        return ProductDetailDto.builder()
                .ptId(productEntity.getPdtId())
                .ptName(productEntity.getPdtName())
                .ptPrice(productEntity.getPdtPrice())
                .ptQuantity(productEntity.getPdtQuantity())
                .build();
    }

    @Transactional
    public void addShoppingCart(String email, ShoppingCartReqDto shoppingCartReqDto) {
        Optional<ProductEntity> optionalProductEntity
                = productsRepository.findById(shoppingCartReqDto.getPdtId());

        if(optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();

            System.out.println(productEntity.getPdtQuantity());
            if(shoppingCartReqDto.getPdtQuantity() > productEntity.getPdtQuantity()) {
                throw new IllegalArgumentException("Product quantity is greater than productQuantity");
            }

            Optional<CartEntity> opt = cartRepository.findByEmailAndPdtId(email, productEntity.getPdtId());

            CartEntity cartEntity;
            if(opt.isPresent()) {
                cartEntity = opt.get();
                cartEntity.setQuantity(shoppingCartReqDto.getPdtQuantity() + cartEntity.getQuantity());
                cartEntity.setPrice( cartEntity.getQuantity() * productEntity.getPdtPrice());
            }else {
                cartEntity = CartEntity.builder()
                        .pdtId(productEntity.getPdtId())
                        .pdtName(productEntity.getPdtName())
                        .price(productEntity.getPdtPrice() * shoppingCartReqDto.getPdtQuantity())
                        .quantity(shoppingCartReqDto.getPdtQuantity())
                        .email(email)
                        .build();
            }
            cartRepository.save(cartEntity);
        }else {
            throw new IllegalArgumentException("Product not found or pdtId miss");
        }
    }

    public List<ShoppingCartReqDto> getShoppingCart(String email) {
        List<CartEntity> carts = cartRepository.findByEmail(email);
        return carts.stream()
                .map( cartEntity -> ShoppingCartReqDto.builder()
                        .pdtId(cartEntity.getPdtId())
                        .pdtName(cartEntity.getPdtName())
                        .pdtPrice(cartEntity.getPrice())
                        .pdtQuantity(cartEntity.getQuantity())
                        .build() )
                .collect(Collectors.toList());
    }

    //장바구니 목록 삭제
    @Transactional
    public void removeItemFromCart(String email, Integer pdtId) {
        Optional<CartEntity> opt = cartRepository.findByEmailAndPdtId(email, pdtId);

        if(opt.isPresent()) {
            cartRepository.delete(opt.get());
        } else {
            throw new IllegalArgumentException("Product not found in cart");
        }
    }

    @Transactional
    public void clearShoppingCart(String email) {
        List<CartEntity> carts = cartRepository.findByEmail(email);

        if(carts.isEmpty()) {
            throw new IllegalArgumentException("Shopping cart is already empty");
        }

        cartRepository.deleteAll(carts);
    }

    // 키워드 검색
    public List<ProductEntity> searchProducts(String keyword) {
        return productsRepository.findByPdtNameContaining(keyword);
    }

    // 가격 필터링
    public List<ProductEntity> filterProductsByPrice(int minPrice, int maxPrice) {
        return productsRepository.filterByPrice(minPrice, maxPrice);
    }
}




