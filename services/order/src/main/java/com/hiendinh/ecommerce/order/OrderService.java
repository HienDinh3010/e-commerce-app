package com.hiendinh.ecommerce.order;

import com.hiendinh.ecommerce.customer.CustomerClient;
import com.hiendinh.ecommerce.exception.BusinessException;
import com.hiendinh.ecommerce.kafka.OrderConfirmation;
import com.hiendinh.ecommerce.kafka.OrderProducer;
import com.hiendinh.ecommerce.orderline.OrderLineRequest;
import com.hiendinh.ecommerce.orderline.OrderLineService;
import com.hiendinh.ecommerce.product.ProductClient;
import com.hiendinh.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        //check the customer, use OpenFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: " +
                        "No customer found with customer id: " + request.customerId()));

        //purchase product -> call product-service
        var purchaseProduct = this.productClient.purchaseProducts(request.products());
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
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseProduct
                )
        );
        //send the order confirmation -> call notification service (kafka)
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(mapper::fromOrder).collect(Collectors.toList());
    }


    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId).map(mapper::fromOrder).orElseThrow( () ->
                new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId))
        );
    }
}
