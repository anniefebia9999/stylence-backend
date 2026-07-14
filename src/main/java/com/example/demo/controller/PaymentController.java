package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Your cart is empty");
        }

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        int amountInPaise = (int) Math.round(total * 100); // Razorpay works in paise, not rupees

        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + user.getId() + "_" + System.currentTimeMillis());

            com.razorpay.Order razorpayOrder = razorpay.orders.create(orderRequest);

            return ResponseEntity.ok(new CreatePaymentOrderResponse(
                    razorpayOrder.get("id"),
                    keyId,
                    amountInPaise,
                    "INR"
            ));
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Failed to create payment order: " + e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/verify")
    public ResponseEntity<?> verifyAndPlaceOrder(@Valid @RequestBody VerifyPaymentRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        // Verify the payment signature — this proves the payment is genuine and wasn't tampered with
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpayOrderId());
            options.put("razorpay_payment_id", request.getRazorpayPaymentId());
            options.put("razorpay_signature", request.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);
            if (!isValid) {
                return ResponseEntity.status(400).body("Payment verification failed. Please contact support.");
            }
        } catch (RazorpayException e) {
            return ResponseEntity.status(400).body("Payment verification error: " + e.getMessage());
        }

        // Payment is verified — now place the actual order (same logic as before)
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
        order.setPaymentMethod("ONLINE (Razorpay: " + request.getRazorpayPaymentId() + ")");
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice()));
        }
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUser(user);

        return ResponseEntity.ok(new OrderResponse(savedOrder));
    }
}