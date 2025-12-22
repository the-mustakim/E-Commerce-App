package com.app.ecom.service;

import com.app.ecom.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(String userId);
}
