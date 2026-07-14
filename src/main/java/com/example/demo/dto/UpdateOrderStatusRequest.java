package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateOrderStatusRequest {
    @NotBlank
    private String status; // PLACED, SHIPPED, DELIVERED, CANCELLED

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}