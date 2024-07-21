package com.hiendinh.ecommerce.order;

import com.hiendinh.ecommerce.customer.CustomerClient;
import com.hiendinh.ecommerce.exception.BusinessException;
import com.hiendinh.ecommerce.orderline.OrderLineRequest;
import com.hiendinh.ecommerce.orderline.OrderLineService;
import com.hiendinh.ecommerce.product.ProductClient;
import com.hiendinh.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(OrderRequest request) {
        //check the customer, use OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: " +
                        "No customer found with customer id: " + request.customerId()));

        //purchase product -> call product-service
        this.productClient.purchaseProducts(request.products());
        //persist order
        var order = this.repository.save(mapper.toOrder(request));
        //persist order-line
        for (PurchaseRequest purchaseRequest: request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

//        var paymentRequest = new PaymentRequest(
//                request.amount(),
//                request.paymentMethod(),
//                order.getId(),
//                order.getReference(),
//                customer
//        );

        //todo: start payment-process

        //send the order confirmation -> call notification service (kafka)
        return null;
    }

}
