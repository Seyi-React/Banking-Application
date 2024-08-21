package com.oluwaseyi.Bank_Demo_Backend.serviceImpl;

import com.oluwaseyi.Bank_Demo_Backend.dto.TransactionDto;


public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
