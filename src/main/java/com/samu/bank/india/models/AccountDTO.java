package com.samu.bank.india.models;

import lombok.Data;

@Data
public class AccountDTO{
    private int accountNumber;
    private String holderName;
    private int amount;
}
