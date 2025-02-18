package com.example.products.service;

import com.example.products.entity.Order;
import com.example.products.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }



    // 내 주문 목록 조회 (이메일 기준)
    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByEmail(email);
    }


    // 특정 주문 상세 조회
    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
