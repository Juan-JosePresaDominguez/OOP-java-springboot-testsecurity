package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.model.Product;
import com.microcompany.productsservice.persistence.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductServiceController.class)
public class ProductServiceControllerTest_WebMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductsRepository repository;

    @Test
    public void givenProducts_whenGetProducts_thenReturnJsonArray() throws Exception {

        Product aProduct = new Product(1L, "Fake product","123-123-1234");

        List<Product> allProducts = Arrays.asList(aProduct);

        given(repository.findAll()).willReturn(allProducts);

        mvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(aProduct.getName())));
    }


}
