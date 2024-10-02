package com.samu.bank.india.models;

import lombok.Data;

@Data
public class AccountTransferDTO {
    long transactionId;
    int fromAccountNumber;
    int toAccountNumber;
    int amount;
}
