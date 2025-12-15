package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.exception.NotFoundException;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest(
                "Samsung",
                "Latest Apple phone",
                BigDecimal.valueOf(123123),
                10,
                "Electronics",
                "img.jpg"
        );

        product = new Product(
                "Samsung",
                "Latest Apple phone",
                BigDecimal.valueOf(123123),
                10,
                "Electronics",
                "img.jpg"
        );
        product.setId(1L);
        product.setActive(true);
    }

    //TEST: addProduct()
    @Test
    void addProduct_shouldSaveAndReturnResponse() {
        when(productRepo.saveAndFlush(any(Product.class))).thenReturn(product);
        ProductResponse productResponse = productService.addProduct(productRequest);
        assertNotNull("Product Response is not available", productResponse);
        assertEquals("Name of the product is wrong", "Samsung", productResponse.getName());
        verify(productRepo).saveAndFlush(any(Product.class));
    }

    //TEST: updateProduct()
    @Test
    void updateProduct_shouldUpdateAndReturnResponse(){
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.saveAndFlush(any(Product.class))).thenReturn(product);
        ProductResponse productResponse = productService.updateProduct(String.valueOf(1),productRequest);
        assertNotNull("Product Response is not available", productResponse);
        assertEquals("Product is not updated..","Samsung",productResponse.getName());
        verify(productRepo).saveAndFlush(any(Product.class));
    }

    //TEST: getAllProducts()
    @Test
    void getAllProducts_shouldReturnAllProducts(){
        when(productRepo.findAllByActiveTrue()).thenReturn(List.of(product));
        List<ProductResponse> productResponsesList = productService.getAllProducts();
        assertEquals("Product list is empty", 1, productResponsesList.size());
    }

    //TEST: getProduct()
    @Test
    void getProduct_shouldReturnProduct(){
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        ProductResponse productResponse = productService.getProduct(String.valueOf(1));
        assertEquals("Product not found","Samsung",productResponse.getName());
    }

    @Test
    void getProduct_shouldThrowIfNotFound() {
        when(productRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.getProduct("999"));
    }

    //TEST: deleteProduct()
    @Test
    void deleteProduct_shouldDeleteProduct(){
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.saveAndFlush(product)).thenReturn(product);
        productService.deleteProduct(String.valueOf(1));
        verify(productRepo).saveAndFlush(product);
    }

    @Test
    void deleteProduct_shouldThrowIfNotFound(){
        when(productRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.deleteProduct("2"));
    }

    //TEST: searchProduct()
    @Test
    void searchProduct_shouldReturnMathingProduct(){
        when(productRepo.searchProducts("Samsung")).thenReturn(List.of(product));
        List<ProductResponse> productResponsesList = productService.searchProduct("Samsung");
        assertEquals("List if empty for given keyword",1, productResponsesList.size());
    }




}
