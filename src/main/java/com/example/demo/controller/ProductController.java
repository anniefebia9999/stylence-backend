package com.example.demo.controller;

import com.example.demo.dto.ProductPublicResponse;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // PUBLIC — used by the storefront. Never includes "remark".
    @GetMapping
    public List<ProductPublicResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductPublicResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(p -> ResponseEntity.ok(new ProductPublicResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ADMIN-ONLY — includes "remark". Used by the Admin Panel product table.
    @GetMapping("/admin/all")
    public List<Product> getAllProductsForAdmin() {
        return productRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updated) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setPrice(updated.getPrice());
                    existing.setOriginalPrice(updated.getOriginalPrice());
                    existing.setImage(updated.getImage());
                    existing.setCategory(updated.getCategory());
                    existing.setDescription(updated.getDescription());
                    existing.setAdditionalImages(updated.getAdditionalImages());
                    existing.setRemark(updated.getRemark());
                    return ResponseEntity.ok(productRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product deleted");
    }
}