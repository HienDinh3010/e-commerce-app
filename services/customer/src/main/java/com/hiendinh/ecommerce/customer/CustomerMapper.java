package com.hiendinh.ecommerce.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.id())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .address(request.address())
                .build();
    }

    public CustomerResponse fromCustomer(Customer response) {
        return new CustomerResponse(
                response.getId(),
                response.getFirstname(),
                response.getLastname(),
                response.getEmail(),
                response.getAddress()
        );
    }
}
