package com.hiendinh.ecommerce.kafka;

import com.hiendinh.ecommerce.customer.CustomerResponse;
import com.hiendinh.ecommerce.order.PaymentMethod;
import com.hiendinh.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
