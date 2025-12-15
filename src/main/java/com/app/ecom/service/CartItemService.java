package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;

public interface CartItemService {
    void addToCart(String userId, CartItemRequest cartItemRequest);
}
