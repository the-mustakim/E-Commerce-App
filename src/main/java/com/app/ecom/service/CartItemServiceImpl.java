package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.dto.CartItemResponse;
import com.app.ecom.exception.NotEnoughQuantityInStockException;
import com.app.ecom.exception.NotFoundException;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepo;
import com.app.ecom.repository.ProductRepo;
import com.app.ecom.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService{

    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;

    public CartItemServiceImpl(CartItemRepo cartItemRepo, ProductRepo productRepo, UserRepo userRepo){
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void addToCart(String userId, CartItemRequest cartItemRequest) {

        Optional<Product> productOpt = productRepo.findById(cartItemRequest.getProductId());
        if(productOpt.isEmpty()){throw new NotFoundException("Product not found with Id: " + cartItemRequest.getProductId());}

        Product product = productOpt.get();
        if(product.getStockQuantity()<cartItemRequest.getQuantity()){throw new NotEnoughQuantityInStockException("Not enough quantity in stock");}

        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: " + userId);}

        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepo.findByUserAndProduct(user,product);
        if(existingCartItem!=null){
            //Update the quantity for existing product
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepo.saveAndFlush(existingCartItem);
        }else{
            //Add item to the cart
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepo.saveAndFlush(cartItem);
        }
    }

    @Override
    public void deleteCart(String userId, Long productId) {
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}

        Optional<Product> productOpt = productRepo.findById(productId);
        if(productOpt.isEmpty()){throw new NotFoundException("Product not found with Id: " + productOpt);}

        CartItem existingCartItem = cartItemRepo.findByUserAndProduct(userOpt.get(),productOpt.get());
        if(existingCartItem!=null){
            cartItemRepo.delete(existingCartItem);
        }else{
            throw new NotFoundException("Cart Item not found for userID : " + userId + " and productID: " + productId);
        }

    }


    @Override
    public List<CartItemResponse> getAllCartItem(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}
        return cartItemRepo.findByUser(userOpt.get()).stream().map(cartItem -> new CartItemResponse(cartItem.getId(),cartItem.getQuantity())).toList();
    }
}
