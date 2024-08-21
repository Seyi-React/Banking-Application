package com.oluwaseyi.Bank_Demo_Backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {

    private String accountName; 
    private String accountNumber;
    private BigDecimal accountBalance;  
    
}
