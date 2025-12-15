package com.app.ecom.controller;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productRequest));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String productId,@RequestBody ProductRequest productRequest){
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productId,productRequest));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String productId){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String,Object>> deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status","200",
                "message","product is deleted"
        ));
    }

    @GetMapping("/search-product")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(productService.searchProduct(keyword));
    }


}
