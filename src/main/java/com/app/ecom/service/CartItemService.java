package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;

import java.util.List;

public interface CartItemService {
    void addToCart(String userId, CartItemRequest cartItemRequest);

    void deleteCart(String userId, Long productId);

    List<CartItemResponse> getAllCartItem(Long userId);
}
