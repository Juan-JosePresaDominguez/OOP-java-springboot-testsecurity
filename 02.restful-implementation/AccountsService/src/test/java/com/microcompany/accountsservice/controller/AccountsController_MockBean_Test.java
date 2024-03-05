package com.microcompany.accountsservice.controller;


import com.microcompany.accountsservice.exception.AccountNotfoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.ontroller.AccountsController;
import com.microcompany.accountsservice.payload.MoneyForOwner;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountsController.class)
class AccountsController_MockBean_Test {

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
    private AccountsController accountsController;

    @MockBean
    private AccountService service;

    @MockBean
    private AccountRepository repository;

    @Test
    void addBalance_OK() {

//        given
        Long id = 1L;
        Long cid = 1L;
        int mount = 20;

        MoneyForOwner moneyForOwner = new MoneyForOwner(cid, mount);

//        when + then
        ResponseEntity<Account> responseEntity = accountsController.addMoney(id, moneyForOwner);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void addBalance_NotAccountFound() {

//        given
        Long id = 1L;
        Long cid = 10L;
        int mount = 20;

        MoneyForOwner moneyForOwner = new MoneyForOwner(id, mount);

//        when + then
        assertThrows(AccountNotfoundException.class, () -> {
            accountsController.addMoney(cid, moneyForOwner);
        });
    }

    @Test
    void deleteUserAccounts_OK() {

//        given
        Long cid = 1L;

//        when + then
        ResponseEntity<Object> responseEntity = accountsController.deleteAccountByUserId(cid);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteUserAccounts_NotAccountFound() {

//        given
        Long cid = 4L;

//        when + then
        assertThrows(AccountNotfoundException.class, () -> {
            accountsController.deleteAccountByUserId(cid);
        });
    }
}