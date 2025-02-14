package com.example.products.service;

import com.example.products.entity.Order;
import com.example.products.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 내 주문 목록 조회
    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByEmail(email);
    }

    // 주문 상세 조회
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
    }
}
