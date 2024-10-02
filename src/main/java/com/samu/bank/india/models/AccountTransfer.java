package com.samu.bank.india.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int fromAccountNumber;
    int toAccountNumber;
    int amount;
    boolean isSuccess;

    public AccountTransfer( int fromAccountNumber, int toAccountNumber, int amount) {
        this.amount = amount;
        this.toAccountNumber = toAccountNumber;
        this.fromAccountNumber = fromAccountNumber;
        this.isSuccess = false;
    }
}
