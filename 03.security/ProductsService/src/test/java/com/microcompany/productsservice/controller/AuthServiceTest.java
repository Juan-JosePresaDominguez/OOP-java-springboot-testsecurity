package com.microcompany.productsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microcompany.productsservice.ProductsServiceApplication;
import com.microcompany.productsservice.model.*;
import com.microcompany.productsservice.persistence.ProductsRepository;
import com.microcompany.productsservice.persistence.UserRepository;
import com.microcompany.productsservice.util.JsonUtil;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);

    String accessToken = null;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductsRepository productsRepository;

    @Autowired
    private UserRepository userRepository;

    /*@BeforeAll
    @Commit // force REAL saving in DB
    public void clean() {
        try {
            logger.info("**** cleaning users...");
            userRepository.deleteUsersByEmail("t@t.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @BeforeEach
    public void setUp() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Fake product", "")
        );

        Mockito.when(productsRepository.findByNameContaining("Fake"))
                .thenReturn(products);

        Mockito.when(productsRepository.findAll())
                .thenReturn(products);

        Mockito.when(productsRepository.save(Mockito.any(Product.class)))
                .thenAnswer(elem -> {
                    Product ap = (Product) elem.getArguments()[0];
                    ap.setId(100L);
                    return ap;
                });
    }

    @Test
    @Order(1)
    void given_existing_user_when_login_success() throws Exception {
        // given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String email = "r@r.com";
        String password = "pa55wrd";
        String enc_password = passwordEncoder.encode(password);

        User aUser = new User(null, email, enc_password, ERole.USER);
        userRepository.save(aUser);

        logger.info("Saved user:" + aUser);

        // when
        AuthRequest authRequest = new AuthRequest(email, password);

        //then
        MvcResult result = mvc.perform(post("/auth/login")
                        .content(JsonUtil.asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        AuthResponse response = new ObjectMapper().readValue(contentAsString, AuthResponse.class);

        logger.info(response.toString());

        accessToken = response.getAccessToken();
    }

    @Test
    @Order(2)
    void given_accesstoken_when_getproducts_then_success() {

        try {
            // given: existing token
            // given_existing_user_when_login_success(); // must  have executed previously in test sequence

            // when
            mvc.perform(get("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + accessToken)
                    )
                    // then
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("Fake product")));

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

}