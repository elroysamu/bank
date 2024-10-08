package com.samu.bank.india.controllers;

import com.samu.bank.india.models.Account;
import com.samu.bank.india.models.AccountDTO;
import com.samu.bank.india.models.AccountTransfer;
import com.samu.bank.india.models.AccountTransferDTO;
import com.samu.bank.india.service.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class BankController {

    private final AccountServiceImpl accountServiceImpl;

    @GetMapping("allAccounts")
    public ResponseEntity<List<Account>> allAccounts(){
        return new ResponseEntity<>(accountServiceImpl.allAccounts(),
                HttpStatus.OK);
    }

    @PostMapping("createAccount")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO){
        return new ResponseEntity<>(accountServiceImpl.createAccount(accountDTO),
                HttpStatus.CREATED);
    }

    @PostMapping("deposit")
    public ResponseEntity<String> deposit(@RequestBody AccountDTO accountDTO){
        return new ResponseEntity<>(accountServiceImpl.deposit(accountDTO.getAccountNumber(), accountDTO.getAmount()),
                HttpStatus.OK);
    }

    @PostMapping("withdrawal")
    public ResponseEntity<String> withdrawal(@RequestBody AccountDTO accountDTO){
        return new ResponseEntity<>(accountServiceImpl.withdrawal(accountDTO.getAccountNumber(), accountDTO.getAmount()),
                HttpStatus.OK);
    }

    @PostMapping("accountTransfer")
    public ResponseEntity<String> accountTransfer(@RequestBody AccountTransferDTO accountTransferDTO) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(accountServiceImpl.accountTransfer(accountTransferDTO),
                HttpStatus.OK);
    }

    @PostMapping("cancelTransfer/{transactionId}")
    public ResponseEntity<String> cancelTransaction(@PathVariable long transactionId){
        return new ResponseEntity<>(accountServiceImpl.cancelTransfer(transactionId),
                HttpStatus.OK);
    }

    @DeleteMapping("deleteAccount")
    public ResponseEntity<String> delete(@RequestBody int accountNumber){
        return new ResponseEntity<>((accountServiceImpl.deleteAccount(accountNumber)),
                HttpStatus.OK);
    }

    @GetMapping("allTransfers")
    public ResponseEntity<List<AccountTransfer>> allTransfers(){
        return new ResponseEntity<>(accountServiceImpl.getAllTransactions(),
                HttpStatus.OK);
    }
}
