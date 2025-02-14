package com.example.products.service;

import com.example.products.entity.Order;
import com.example.products.repository.OrderRepository;
<<<<<<< HEAD
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
=======
import org.springframework.stereotype.Service;

import java.util.List;
>>>>>>> dd2e0bb (내 주문 목록 보기 기능 추가(응답o, orders 테이블 문제발생))

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

<<<<<<< HEAD
    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // 내 주문 목록 조회 (이메일 기준)
=======
    // 내 주문 목록 조회
>>>>>>> dd2e0bb (내 주문 목록 보기 기능 추가(응답o, orders 테이블 문제발생))
    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByEmail(email);
    }

<<<<<<< HEAD
    // 특정 주문 상세 조회
    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // 전체 주문 목록 조회
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
=======
    // 주문 상세 조회
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));
>>>>>>> dd2e0bb (내 주문 목록 보기 기능 추가(응답o, orders 테이블 문제발생))
    }
}
