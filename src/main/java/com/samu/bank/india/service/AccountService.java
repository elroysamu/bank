package com.samu.bank.india.service;

import com.samu.bank.india.models.Account;
import com.samu.bank.india.models.AccountDTO;
import com.samu.bank.india.models.AccountTransfer;
import com.samu.bank.india.models.AccountTransferDTO;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountDTO account);
    String deposit(int accountNumber, int amount);
    String withdrawal(int accountNumber, int amount);
    String deleteAccount(int accountNumber);
    List<Account> allAccounts();
    String accountTransfer(AccountTransferDTO accountTransferDTO);
    String cancelTransfer(long transactionId);
    List<AccountTransfer> getAllTransactions();
}
