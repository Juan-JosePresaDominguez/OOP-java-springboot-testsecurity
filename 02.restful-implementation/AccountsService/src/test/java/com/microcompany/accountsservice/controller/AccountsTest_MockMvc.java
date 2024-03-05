package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.payload.MoneyForOwner;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import com.microcompany.accountsservice.util.JsonUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("classpath:datos_prueba.sql")
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountsTest_MockMvc {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Test
    @Order(1)
    void addmoney_OK() throws Exception {

//        given
        Long id = 1L;
        Long cid = 1L;
        int mount = 20;

        MoneyForOwner moneyForOwner = new MoneyForOwner(cid, mount);


//        when + then
        mvc.perform(put("/accounts/addmoney/" + id)
                        .content(JsonUtil.asJsonString(moneyForOwner))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.balance", is(40)))
        ;
    }

    @Test
    @Order(2)
    void addmoney_NotAccountFound() throws Exception {

//        given
        Long id = 10L;
        Long cid = 1L;
        int mount = 20;

        MoneyForOwner moneyForOwner = new MoneyForOwner(cid, mount);

//        when + then
        mvc.perform(put("/accounts/addmoney/" + id)
                        .content(JsonUtil.asJsonString(moneyForOwner))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @Order(3)
    void deleteUserAccounts_OK() throws Exception {

//        given
        Long cid = 1L;

//        when + then
        mvc.perform(delete("/accounts/user/" + cid).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void deleteUserAccounts_NotAccountFound() throws Exception {

//        given
        Long cid = 4L;

//        when + then
        mvc.perform(delete("/accounts/user/" + cid).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}