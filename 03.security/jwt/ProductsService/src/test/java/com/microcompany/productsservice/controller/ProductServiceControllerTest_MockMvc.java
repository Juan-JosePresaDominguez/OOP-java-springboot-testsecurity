package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.ProductsServiceApplication;
import com.microcompany.productsservice.model.Product;
import com.microcompany.productsservice.persistence.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ProductsServiceApplication.class)
@AutoConfigureMockMvc
//@TestPropertySource( locations = "classpath:application-integrationtest.properties")
class ProductServiceControllerTest_MockMvc {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductsRepository repository;

    @Test
    public void givenProducts_whenGetProducts_thenStatus200() throws Exception {
        Product nuevoProd = new Product();
        nuevoProd.setName("Nuevo Prod");
        repository.save(nuevoProd);

        mvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Nuevo Prod")));
    }
}