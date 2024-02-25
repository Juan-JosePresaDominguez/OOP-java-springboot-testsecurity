package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.model.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "classpath:testing.sql")
//@Sql(value = "classpath:testing_clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceControllerTest {

    @Autowired
    private ProductServiceController controller;

    @Test
    @Order(2)
    void givenProducts_whengetAllProducts_thenIsNotNull() {
        ResponseEntity<List<Product>> response = controller.getAllProducts();
        System.out.println("response:" + response.getBody());

        assertThat(response.getStatusCode().value())
                .isEqualTo(HttpStatus.OK.value());

        assertThat(response.getBody())
                .isNotNull();
    }

    @Test
    @Order(1)
    void givenProducts_whenVaildCreateProduct_thenIsCreatedAndHaveId() {
        Product newProduct = new Product(null, "Nuevo producto", "123-123-1234");

        ResponseEntity<Product> response = controller.createProduct(newProduct);
        System.out.println("***** response:" + response);

        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody().getId()).isGreaterThan(0);

    }
}