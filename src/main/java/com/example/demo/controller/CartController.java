package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Helper: get the logged-in user from the JWT-authenticated request
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName(); // set by JwtAuthFilter
        return userRepository.findByEmail(email).orElse(null);
    }

    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        List<CartItemResponse> items = cartItemRepository.findByUser(user)
                .stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest request, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        Product product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) return ResponseEntity.badRequest().body("Product not found");

        CartItem existing = cartItemRepository.findByUserAndProductId(user, product.getId()).orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            cartItemRepository.save(existing);
            return ResponseEntity.ok(new CartItemResponse(existing));
        } else {
            CartItem newItem = new CartItem(user, product, request.getQuantity());
            cartItemRepository.save(newItem);
            return ResponseEntity.ok(new CartItemResponse(newItem));
        }
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long itemId,
                                             @Valid @RequestBody UpdateCartRequest request,
                                             Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(404).body("Cart item not found");
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return ResponseEntity.ok(new CartItemResponse(item));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(404).body("Cart item not found");
        }

        cartItemRepository.delete(item);
        return ResponseEntity.ok("Removed");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) return ResponseEntity.status(401).body("User not found");

        cartItemRepository.deleteByUser(user);
        return ResponseEntity.ok("Cart cleared");
    }
}