package com.oluwaseyi.Bank_Demo_Backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oluwaseyi.Bank_Demo_Backend.dto.BankResponse;
import com.oluwaseyi.Bank_Demo_Backend.dto.CreditDebitRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.EnquiryRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.LoginDto;
import com.oluwaseyi.Bank_Demo_Backend.dto.TransferRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.UserRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.getAccountDto;
import com.oluwaseyi.Bank_Demo_Backend.serviceImpl.UserService;

@RestController
@RequestMapping("/api/users")
public class UserContoller {

    @Autowired
      UserService userService;


      @PostMapping
      public BankResponse createAccount (@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
      }

      @PostMapping("/balanceEnquiry")
      public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiry) {
        return userService.balanceEnquiry(enquiry);
      }

      @PostMapping("/nameEnquiry") 
      public String nameEnquiry(@RequestBody EnquiryRequest enquiry) {
        return userService.nameEnquiry(enquiry);
      }

      @PostMapping("/credit")
      public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAccount(creditDebitRequest);
      }


      @PostMapping("/debit")
      public BankResponse debitAccount (@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAccount(creditDebitRequest);
      }

      @PostMapping("/transfer")
      public BankResponse transfer ( @RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
      }

      @PostMapping("/getAccountNumber")
      public String getAccountNumber(@RequestBody getAccountDto getAccountDto) {
        return userService.getAccount(getAccountDto);
      }

      @PostMapping("/login")
      public BankResponse login (@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
      }
}
 