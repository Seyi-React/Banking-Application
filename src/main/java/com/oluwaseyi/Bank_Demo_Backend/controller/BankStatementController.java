package com.oluwaseyi.Bank_Demo_Backend.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oluwaseyi.Bank_Demo_Backend.entity.Transaction;
import com.oluwaseyi.Bank_Demo_Backend.serviceImpl.BankStatementService;

@RestController
public class BankStatementController {

    private final BankStatementService bankStatementService;

    public BankStatementController(BankStatementService bankStatementService) {
        this.bankStatementService = bankStatementService;
    }

    @GetMapping("/api/statement")
    public ResponseEntity<List<Transaction>> getBankStatement(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        // Retrieve the list of transactions for the specified account and date range
        List<Transaction> transactions = bankStatementService.generateStatement(accountNumber, startDate, endDate);
        
        // Return the list of transactions as JSON
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/api/statement/pdf")
    public ResponseEntity<byte[]> getBankStatementPdf(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        // Generate the PDF file
        byte[] pdfData = bankStatementService.generateStatementPdf(accountNumber, startDate, endDate);
        
        // Set the content type and headers for the PDF response
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BankStatement.pdf");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        
        // Return the PDF as a byte array
        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }
}
