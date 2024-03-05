package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.ontroller.AccountsController;
import com.microcompany.accountsservice.payload.MoneyForOwner;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import com.microcompany.accountsservice.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountsController.class)
class AccountsTest_WebMvcTest {

    @BeforeEach
    public void setUp() {

        Account account = new Account(1L, "tipo1", new Date(), 20, 1L, null);

        Mockito.when(service.addBalance(1L, 20, 1L))
                .thenReturn(account);

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(account));

        Mockito.when(service.addBalance(10L, 20, 1L))
                .thenThrow(AccountNotfoundException.class);

        Mockito.when(repository.findById(10L))
                .thenThrow(AccountNotfoundException.class);

        Mockito.when(repository.findByOwnerId(4L))
                .thenReturn(new ArrayList<>());

        Mockito.doThrow(new AccountNotfoundException()).when(service).deleteAccountsUsingOwnerId(4L);

        Mockito.when(repository.save(Mockito.any(Account.class)))
                .thenAnswer(elem -> {
                    Account ac = (Account) elem.getArguments()[0];
                    ac.setId(100L);
                    return ac;
                });
    }


    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;

    @MockBean
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
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
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