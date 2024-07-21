package com.hiendinh.ecommerce.order;

import com.hiendinh.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,

        @Positive(message = "Order amount need to be positive")
        BigDecimal amount,

        @NotNull(message = "Payment method should be precised")
        PaymentMethod paymentMethod,

        @NotNull(message = "CustomerId should not be null")
        @NotEmpty(message = "CustomerId should not be null")
        @NotBlank(message = "CustomerId should not be null")
        String customerId,

        @NotEmpty(message = "You needs to purchase at least one product")
        List<PurchaseRequest> products
) {

}
