package com.oluwaseyi.Bank_Demo_Backend.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.stereotype.Component;

import com.oluwaseyi.Bank_Demo_Backend.dto.TransactionDto;
import com.oluwaseyi.Bank_Demo_Backend.entity.Transaction;
import com.oluwaseyi.Bank_Demo_Backend.repository.TransactionRepository;

@Component
public class Transactionimpl  implements TransactionService {


    @Autowired TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transaction) {
        Transaction trans = Transaction.builder()
         .accountNumber(transaction.getAccountNumber())
         .amount(transaction.getAmount())
         .status("SUCCESS")
         .transactionType(transaction.getTransactionType())
        .build();

        transactionRepository.save(trans);
        System.out.println("transaction saved successfully");
    }
    
}
