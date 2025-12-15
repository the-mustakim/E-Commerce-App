package com.app.ecom.exception;

public class NotEnoughQuantityInStockException extends RuntimeException{
    public NotEnoughQuantityInStockException(String msg){
        super(msg);
    }
}
