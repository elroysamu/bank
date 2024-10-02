package com.samu.bank.india.repository;

import com.samu.bank.india.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
    Account findByAccountNumber(int accountNumber);
    void deleteByAccountNumber(int accountNumber);
}
