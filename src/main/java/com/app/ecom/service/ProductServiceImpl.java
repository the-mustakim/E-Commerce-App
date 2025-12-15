package com.app.ecom.service;

import com.app.ecom.model.Product;
import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.exception.NotFoundException;
import com.app.ecom.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepo productRepo;

    public ProductServiceImpl(ProductRepo productRepo){
        this.productRepo = productRepo;
    }

    @Transactional
    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        return mapToProductResponse(productRepo.saveAndFlush(mapToProduct(productRequest)));
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(String productId, ProductRequest productRequest) {
        Optional<Product> productOp = productRepo.findById(Long.valueOf(productId));
        if(productOp.isEmpty()){throw new NotFoundException("Product not found with Id: " + productId);}
        Product product = productOp.get();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        return mapToProductResponse(productRepo.saveAndFlush(product));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepo.findAllByActiveTrue().stream().map(this::mapToProductResponse).toList();
    }

    @Override
    public ProductResponse getProduct(String productId) {
        Optional<Product> productOp = productRepo.findById(Long.valueOf(productId));
        if(productOp.isEmpty()){throw new NotFoundException("Product not found with Id: " + productId);}
        return mapToProductResponse(productOp.get());
    }

    @Transactional
    @Override
    public void deleteProduct(String productId) {
        Optional<Product> productOp = productRepo.findById(Long.valueOf(productId));
        if(productOp.isEmpty()){throw new NotFoundException("Product not found with Id: " + productId);}
        Product product = productOp.get();
        product.setActive(false);
        productRepo.saveAndFlush(product);
    }

    @Override
    public List<ProductResponse> searchProduct(String keyword) {
        return productRepo.searchProducts(keyword).stream().map(this::mapToProductResponse).toList();
    }

    public Product mapToProduct(ProductRequest productRequest){
        return new Product(productRequest.getName(),productRequest.getDescription(),productRequest.getPrice(),productRequest.getStockQuantity(),productRequest.getCategory(),productRequest.getImageUrl());
    }

    public ProductResponse mapToProductResponse(Product product){
        return new ProductResponse(product.getId(),product.getName(),product.getDescription(),product.getPrice(),product.getStockQuantity(),product.getCategory(),product.getImageUrl(),product.getActive());
    }

}
