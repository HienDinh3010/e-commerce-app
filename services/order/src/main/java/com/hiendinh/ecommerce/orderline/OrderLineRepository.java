package com.hiendinh.ecommerce.orderline;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findAllByOrderId(Integer orderId);
}
