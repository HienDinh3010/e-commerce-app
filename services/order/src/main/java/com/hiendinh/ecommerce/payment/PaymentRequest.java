package com.hiendinh.ecommerce.payment;

import com.hiendinh.ecommerce.customer.CustomerResponse;
import com.hiendinh.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}