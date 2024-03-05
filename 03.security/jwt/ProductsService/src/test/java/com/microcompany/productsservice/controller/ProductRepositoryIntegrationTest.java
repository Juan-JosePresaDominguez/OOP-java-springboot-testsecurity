package com.microcompany.productsservice.controller;

import com.microcompany.productsservice.model.Product;
import com.microcompany.productsservice.persistence.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductsRepository productsRepository;

    @Test
    public void whenFindByName_thenReturnProduct() {
        // given
        Product aProduct = new Product(null, "Fake Product","123-123-1234");
        entityManager.persist(aProduct);
        entityManager.flush();

        // when
        Product found = productsRepository.findByName(aProduct.getName());

        // then
        assertThat(found.getName())
                .isEqualTo(aProduct.getName());
    }

}
