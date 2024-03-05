package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.model.Product;
import com.microcompany.productsservice.persistence.ProductsRepository;
import com.microcompany.productsservice.service.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class ProductServiceIntegrationTest {

    @TestConfiguration
    static class ProductServiceImplTestContextConfiguration {
        @Bean
        public ProductsService productsService() {
            return new ProductsService();
        }
    }


    @BeforeEach
    public void setUp() {
        List<Product> products = Arrays.asList(
                new Product(1L,"Fake product","")
        );
        Mockito.when(productsRepository.findByNameContaining("Fake"))
                .thenReturn(products);
    }

    @Autowired
    private ProductsService productsService;
    @MockBean
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsRepository productRepo;

    @Test
    public void whenValidText_thenProductsShouldBeFound() {
        String text = "Fake";
        List<Product> found = productsService.getProductsByText(text);
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).contains("Fake");

    }

}
