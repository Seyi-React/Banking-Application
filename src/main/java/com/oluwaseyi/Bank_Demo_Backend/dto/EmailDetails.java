package com.oluwaseyi.Bank_Demo_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String receipient;
    private String subject;
    private String attachment;
    private String messageBody;
}
