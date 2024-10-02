package com.samu.bank.india.service;

import com.samu.bank.india.exception.InsufficientFundsException;
import com.samu.bank.india.models.Account;
import com.samu.bank.india.models.AccountDTO;
import com.samu.bank.india.models.AccountTransfer;
import com.samu.bank.india.models.AccountTransferDTO;
import com.samu.bank.india.repository.AccountRepository;
import com.samu.bank.india.repository.AccountTransferRepository;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class AccountServiceImpl implements AccountService {
    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final AccountTransferRepository accountTransferRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<Long, Future<String>> transactionMap = new ConcurrentHashMap<>();
    private final TransactionTemplate transactionTemplate;
    private final FailedTransferService failedTransferService;

    public AccountServiceImpl(ModelMapper modelMapper,
                              AccountRepository accountRepository,
                              AccountTransferRepository accountTransferRepository,
                              TransactionTemplate transactionTemplate,
                              FailedTransferService failedTransferService) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
        this.accountTransferRepository = accountTransferRepository;
        this.transactionTemplate = transactionTemplate;
        this.failedTransferService = failedTransferService;
    }


    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account savedAccount = accountRepository.save(modelMapper.map(accountDTO,Account.class));
        return modelMapper.map(savedAccount, AccountDTO.class);
    }

    @Override
    public String deposit(int accountNumber, int amount) {
        Account accountToDeposit = accountRepository.findByAccountNumber(accountNumber);
        accountToDeposit.getBalance().getAndAdd(amount);
        accountRepository.save(accountToDeposit);
        return "deposited";
    }

    @Override
    public String withdrawal(int accountNumber, int amount) {
        Account accountToWithdraw = accountRepository.findByAccountNumber(accountNumber);
        long currentBalance;
        do {
            currentBalance = accountToWithdraw.getBalance().get();
            if (currentBalance < amount) {
                throw new InsufficientFundsException("Your bank account does not have enough balance." +
                        " Check your balance and try again.");
            }
        } while (!accountToWithdraw.getBalance().compareAndSet(currentBalance, currentBalance - amount));
        accountRepository.save(accountToWithdraw);
        return "withdrawal successful";
    }

    @Override
    public String deleteAccount(int accountNumber) {
        accountRepository.deleteByAccountNumber(accountNumber);
        return "account deleted";
    }

    @Override
    public List<Account> allAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public String accountTransfer(AccountTransferDTO accountTransferDTO) {
        Long transactionId = accountTransferDTO.getTransactionId();
        Future<String> currentTransferFuture = executor.submit(() -> transactionTemplate.execute(status -> {
            AccountTransfer accountTransfer = new AccountTransfer(
                    accountTransferDTO.getFromAccountNumber(),
                    accountTransferDTO.getToAccountNumber(),
                    accountTransferDTO.getAmount()
            );
            try {
                logger.info("samu - withdrawal under process....");
                withdrawal(accountTransferDTO.getFromAccountNumber(), accountTransferDTO.getAmount());
                logger.info("samu - withdrawal completed!");

                logger.info("samu - deposit under process.....");
                Thread.sleep(5000);

                deposit(accountTransferDTO.getToAccountNumber(), accountTransferDTO.getAmount());
                logger.info("samu - deposit completed!");
                accountTransfer.setSuccess(true);
                accountTransferRepository.save(accountTransfer);
                return "Transfer successful";

            } catch (InterruptedException e) {
                status.setRollbackOnly();
                failedTransferService.saveFailedTransfer(accountTransfer);
                throw new RuntimeException("Transaction was canceled!");
            }
        }));
        transactionMap.put(transactionId, currentTransferFuture);
        try {
            return currentTransferFuture.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            currentTransferFuture.cancel(true);
            throw new RuntimeException("Transaction timeout: Operation took too long and was canceled");
        } catch (Exception e) {
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }
    }

    @Override
    public String cancelTransfer(long transactionId){
        Future<String> transaction = transactionMap.get(transactionId);
        if (transaction != null){
            boolean isCanceled = transaction.cancel(true);
            if (isCanceled) {
                transactionMap.remove(transactionId);
                return "Transaction " + transactionId + " canceled successfully.";
            } else {
                return "Failed to cancel transaction " + transactionId;
            }
        }else{
            return "no ongoing transaction found with id "+ transactionId;
        }
    }

    @Override
    public List<AccountTransfer> getAllTransactions() {
        return accountTransferRepository.findAll();
    }
}