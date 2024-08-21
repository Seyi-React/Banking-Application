package com.oluwaseyi.Bank_Demo_Backend.serviceImpl;

import com.oluwaseyi.Bank_Demo_Backend.dto.BankResponse;
import com.oluwaseyi.Bank_Demo_Backend.dto.CreditDebitRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.EnquiryRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.LoginDto;
import com.oluwaseyi.Bank_Demo_Backend.dto.TransferRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.UserRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.getAccountDto;

public interface UserService {
    
    BankResponse createAccount(UserRequest UserRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest transferRequest);
    String getAccount(getAccountDto getAccount);
    BankResponse login(LoginDto loginDto);
}
