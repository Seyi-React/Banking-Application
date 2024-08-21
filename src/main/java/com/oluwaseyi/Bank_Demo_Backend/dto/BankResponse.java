package com.oluwaseyi.Bank_Demo_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponse {
    private String respondCode;
    private String responseMessage;
    private AccountInfo accountInfo;
}
