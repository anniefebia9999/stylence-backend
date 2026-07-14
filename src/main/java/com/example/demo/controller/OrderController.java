package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    @Transactional
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody PlaceOrderRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Your cart is empty");
        }

        Address address = new Address();
        address.setFullName(request.getShippingAddress().getFullName());
        address.setPhone(request.getShippingAddress().getPhone());
        address.setAddressLine1(request.getShippingAddress().getAddressLine1());
        address.setAddressLine2(request.getShippingAddress().getAddressLine2());
        address.setCity(request.getShippingAddress().getCity());
        address.setState(request.getShippingAddress().getState());
        address.setPincode(request.getShippingAddress().getPincode());

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setTotalAmount(total);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            );
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);

        return ResponseEntity.ok(new OrderResponse(savedOrder));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        List<OrderResponse> response = orders.stream().map(OrderResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    // ----- Admin-only endpoints -----

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        List<AdminOrderResponse> response = orders.stream().map(AdminOrderResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(request.getStatus());
                    orderRepository.save(order);
                    return ResponseEntity.ok(new AdminOrderResponse(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}