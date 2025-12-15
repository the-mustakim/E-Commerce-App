package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> addToCart(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest){
        cartItemService.addToCart(userId, cartItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
