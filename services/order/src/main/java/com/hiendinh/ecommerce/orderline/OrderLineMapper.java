package com.hiendinh.ecommerce.orderline;

import com.hiendinh.ecommerce.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.orderId())
                .order(Order.builder()
                        .id(request.orderId())
                        .build())
                .quantity(request.quantity())
                .productId(request.productId())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(orderLine.getId(), orderLine.getQuantity());
    }
}
