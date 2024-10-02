package com.samu.bank.india.service;

import com.samu.bank.india.models.AccountTransfer;
import com.samu.bank.india.repository.AccountTransferRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
public class FailedTransferServiceImpl implements FailedTransferService{

    @Autowired
    private AccountTransferRepository accountTransferRepository;

    private final Logger logger = LoggerFactory.getLogger(FailedTransferServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransfer(AccountTransfer accountTransfer) {
        try {
            logger.info("samu - Attempting to save failed transfer: {}", accountTransfer);
            accountTransferRepository.save(accountTransfer);  // Save using a new transaction
            logger.info(" samu - Failed transfer saved successfully: {}", accountTransfer);
        } catch (Exception e) {
            logger.error("Failed to save transfer: ", e);
        }
    }
}
