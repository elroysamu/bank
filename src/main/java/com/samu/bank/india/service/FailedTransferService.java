package com.samu.bank.india.service;

import com.samu.bank.india.models.AccountTransfer;

public interface FailedTransferService {
    void saveFailedTransfer(AccountTransfer accountTransfer);
}
