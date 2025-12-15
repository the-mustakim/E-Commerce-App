package com.app.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>>handleNotFoundException(NotFoundException notFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status","404",
                "message",notFoundException.getMessage()
        ));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> handleNotEnoughQuantityInStockException(NotEnoughQuantityInStockException notEnoughQuantityInStockException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", "400",
                "message", notEnoughQuantityInStockException.getMessage()
        ));
    }

}
