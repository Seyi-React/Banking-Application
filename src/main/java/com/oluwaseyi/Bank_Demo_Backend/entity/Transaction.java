package com.oluwaseyi.Bank_Demo_Backend.entity;

import java.math.BigDecimal;
// import java.time.LocalDate;
import java.time.LocalDateTime;

// import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// import jakarta.persistence.*;
// import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;
    private String  transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
    private LocalDateTime createdAt;

    
}
