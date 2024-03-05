package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
class ProductServiceControllerTest_Simple {
    @Autowired
    private ProductServiceController controller;

    @Test
    public void givenProducts_whenGetProducts_thenIsNotNull() throws Exception {
        ResponseEntity<List<Product>> productos = controller.getAllProducts();
        assertThat(productos.getStatusCode().value()).isEqualTo(HttpStatus.OK);
        assertThat(productos.getBody()).isNotNull();
    }
}