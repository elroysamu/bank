package com.samu.bank.india.repository;

import com.samu.bank.india.models.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Integer> {
}
