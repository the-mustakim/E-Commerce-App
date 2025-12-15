package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse updateProduct(String productId, ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(String productId);

    void deleteProduct(String productId);

    List<ProductResponse> searchProduct(String keyword);
}
