package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlaceOrderRequest {
    @Valid
    @NotNull
    private AddressRequest shippingAddress;

    @NotBlank
    private String paymentMethod; // "COD" for now

    public AddressRequest getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressRequest shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}