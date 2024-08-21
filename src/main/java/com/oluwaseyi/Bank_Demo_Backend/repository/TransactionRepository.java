package com.oluwaseyi.Bank_Demo_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oluwaseyi.Bank_Demo_Backend.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
    
}
