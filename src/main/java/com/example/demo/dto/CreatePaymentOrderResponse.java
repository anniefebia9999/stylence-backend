package com.example.demo.dto;

public class CreatePaymentOrderResponse {
    private String razorpayOrderId;
    private String keyId;
    private int amountInPaise;
    private String currency;

    public CreatePaymentOrderResponse(String razorpayOrderId, String keyId, int amountInPaise, String currency) {
        this.razorpayOrderId = razorpayOrderId;
        this.keyId = keyId;
        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public String getKeyId() { return keyId; }
    public int getAmountInPaise() { return amountInPaise; }
    public String getCurrency() { return currency; }
}