package com.oluwaseyi.Bank_Demo_Backend.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oluwaseyi.Bank_Demo_Backend.config.JwtTokenProvider;
import com.oluwaseyi.Bank_Demo_Backend.dto.AccountInfo;
import com.oluwaseyi.Bank_Demo_Backend.dto.BankResponse;
import com.oluwaseyi.Bank_Demo_Backend.dto.CreditDebitRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.EmailDetails;
import com.oluwaseyi.Bank_Demo_Backend.dto.EnquiryRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.LoginDto;
import com.oluwaseyi.Bank_Demo_Backend.dto.TransactionDto;
import com.oluwaseyi.Bank_Demo_Backend.dto.TransferRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.UserRequest;
import com.oluwaseyi.Bank_Demo_Backend.dto.getAccountDto;
import com.oluwaseyi.Bank_Demo_Backend.entity.Role;
import com.oluwaseyi.Bank_Demo_Backend.entity.User;
import com.oluwaseyi.Bank_Demo_Backend.repository.UserRepository;
import com.oluwaseyi.Bank_Demo_Backend.utils.AccountUtils;

// import org.apache.el.stream.Optional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

        @Autowired
        UserRepository userRepository;

        @Autowired
        EmailService emailService;

        @Autowired
        TransactionService transactionService;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        AuthenticationManager authenticationManager;


        @Autowired
        JwtTokenProvider jwtTokenProvider;

        private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

        // An implementation class that creates an account
        @Override
        public BankResponse createAccount(UserRequest userRequest) {

                // i am checking if the account number already exists from the client side and
                // checking in my DB
                if (userRepository.existsByEmail(userRequest.getEmail())) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                // After checking if it doesnt exisit then create an account for the user by
                // providing this from these infos from the client side
                User newUser = User.builder().firstName(userRequest.getFirstName())
                                .lastName(userRequest.getLastName())
                                .address(userRequest.getAddress())
                                .alternativeNumber(userRequest.getAlternativeNumber())
                                .gender(userRequest.getGender())
                                .otherName(userRequest.getOtherName())
                                .email(userRequest.getEmail())
                                .password(passwordEncoder.encode(userRequest.getPassword()))
                                .phoneNumber(userRequest.getPhoneNumber())
                                .stateOfOrigin(userRequest.getStateOfOrigin())
                                .status("ACTIVE")
                                .role(Role.valueOf("ROLE_ADMIN"))
                                .accountNumber(AccountUtils.accountGenerator())
                                .accountBalance(BigDecimal.ZERO)
                                .build();

                User savedUser = userRepository.save(newUser);

                // Log saved user info
                logger.info("User saved successfully: {}", savedUser);

                // Send an email to the customer
                EmailDetails emailDetails = EmailDetails.builder()
                                .receipient(savedUser.getEmail())
                                .subject("ACCOUNT CREATION")
                                .messageBody("Congratulations! Your account has been successfully created. \n Your account number is "
                                                + savedUser.getAccountNumber())
                                .build();

                try {
                        emailService.sendEmailAlert(emailDetails);
                        logger.info("Email sent successfully to {}", savedUser.getEmail());
                } catch (Exception e) {
                        logger.error("Error sending email: ", e);
                }

                return BankResponse.builder()
                                .respondCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(savedUser.getAccountBalance())
                                                .accountNumber(savedUser.getAccountNumber())
                                                .accountName(savedUser.getFirstName() + " " + savedUser.getLastName()
                                                                + " " + savedUser.getOtherName())
                                                .build())
                                .build();
        }

        public BankResponse login(LoginDto loginDto) {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
                );
            
                // Temporarily comment out the email sending logic
                // EmailDetails loginAlert = EmailDetails.builder()
                //     .subject("You are logged in")
                //     .receipient(loginDto.getEmail())
                //     .messageBody("You logged into your account")
                //     .build();
            
                // emailService.sendEmailAlert(loginAlert);
            
                return BankResponse.builder()
                    .respondCode("Login Success")
                    .responseMessage(jwtTokenProvider.generateToken(authentication))
                    .build();
            }
            

        // An implementation class to check the balance of an account
        @Override
        public BankResponse balanceEnquiry(EnquiryRequest request) {
                boolean isAccountAvailable = userRepository.existsByAccountNumber(request.getAccountNumber());
                if (!isAccountAvailable) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.ACCOUNT_DO_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT__DO_NOT_EXIST_MESSSAGE)
                                        .accountInfo(null)
                                        .build();
                }
                User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
                return BankResponse.builder()
                                .respondCode(AccountUtils.ACCOUNT_FOUND_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(foundUser.getAccountBalance())
                                                .accountNumber(request.getAccountNumber())
                                                .accountName(foundUser.getFirstName() + " " + foundUser.getLastName()
                                                                + " " + foundUser.getOtherName())
                                                .build())
                                .build();
        }

        // An implementation to make a name enquiry of an account
        @Override
        public String nameEnquiry(EnquiryRequest request) {
                Boolean isAccountAvailable = userRepository.existsByAccountNumber(request.getAccountNumber());
                if (!isAccountAvailable) {
                        return AccountUtils.ACCOUNT__DO_NOT_EXIST_MESSSAGE;
                }
                User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
                return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
        }

        // An implementation class to credit a particular account
        @Override
        public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
                Boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());

                if (!isAccountExist) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.ACCOUNT_DO_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT__DO_NOT_EXIST_MESSSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
                userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
                userRepository.save(userToCredit);

                TransactionDto transactionDto = TransactionDto.builder()
                                .accountNumber(userToCredit.getAccountNumber())
                                .amount(creditDebitRequest.getAmount())
                                .transactionType("CREDIT")
                                .status("SUCCESS")
                                .build();

                transactionService.saveTransaction(transactionDto);

                return BankResponse.builder()
                                .respondCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                                .accountInfo(AccountInfo.builder()
                                                .accountBalance(userToCredit.getAccountBalance())
                                                .accountNumber(userToCredit.getAccountNumber())
                                                .accountName(userToCredit.getFirstName() + " "
                                                                + userToCredit.getLastName()
                                                                + " " + userToCredit.getOtherName())
                                                .build())
                                .build();
        }

        // An implementation class to debit a particular account
        @Override
        public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
                Boolean isAccountExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
                if (!isAccountExist) { // Change this condition
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.ACCOUNT_DO_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT__DO_NOT_EXIST_MESSSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
                BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
                BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();

                if (availableBalance.intValue() < debitAmount.intValue()) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                                        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                                        .build();
                } else {
                        userToDebit.setAccountBalance(
                                        userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
                        userRepository.save(userToDebit);

                        TransactionDto transactionDto = TransactionDto.builder()
                                        .accountNumber(userToDebit.getAccountNumber())
                                        .amount(creditDebitRequest.getAmount())
                                        .transactionType("DEBIT")
                                        .status("SUCCESS")
                                        .build();

                        transactionService.saveTransaction(transactionDto);

                        return BankResponse.builder()
                                        .respondCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                                        .accountInfo(AccountInfo.builder()
                                                        .accountBalance(userToDebit.getAccountBalance())
                                                        .accountName(userToDebit.getFirstName() + " "
                                                                        + userToDebit.getLastName() + " "
                                                                        + userToDebit.getOtherName())
                                                        .accountNumber(userToDebit.getAccountNumber())
                                                        .build())
                                        .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                                        .build();
                }
        }

        // Implementation class to tranfer amount from one account to another
        @Override
        public BankResponse transfer(TransferRequest transferRequest) {

                System.out.println("Transfer Request: " + transferRequest);
                System.out.println("Checking for destination account number: "
                                + transferRequest.getDestinationAccountNumber());

                boolean isDestinationAccountExist = userRepository
                                .existsByAccountNumber(transferRequest.getDestinationAccountNumber());

                if (!isDestinationAccountExist) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.DESTINATION_ACCOUNT_DOES_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.DESTINATION_ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                User sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
                if (sourceAccountUser == null) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.SOURCE_ACCOUNT_DOES_NOT_EXIST_CODE)
                                        .responseMessage(AccountUtils.SOURCE_ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
                        return BankResponse.builder()
                                        .respondCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                                        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                                        .accountInfo(null)
                                        .build();
                }

                // Debit the source account
                sourceAccountUser.setAccountBalance(
                                sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
                userRepository.save(sourceAccountUser);

                // Credit the destination account
                User destinationAccountUser = userRepository
                                .findByAccountNumber(transferRequest.getDestinationAccountNumber());
                destinationAccountUser.setAccountBalance(
                                destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
                userRepository.save(destinationAccountUser);

                // Send debit alert email to source account user
                EmailDetails debitAlert = EmailDetails.builder()
                                .receipient(sourceAccountUser.getEmail())
                                .subject("DEBIT ALERT")
                                .messageBody("The sum of " + transferRequest.getAmount()
                                                + " has been deducted from your account! Your current balance is "
                                                + sourceAccountUser.getAccountBalance())
                                .build();
                emailService.sendEmailAlert(debitAlert);

                // Send credit alert email to destination account user
                EmailDetails creditAlert = EmailDetails.builder()
                                .receipient(destinationAccountUser.getEmail())
                                .subject("CREDIT ALERT") // Corrected the subject
                                .messageBody("The sum of " + transferRequest.getAmount()
                                                + " has been credited to your account! Your current balance is "
                                                + destinationAccountUser.getAccountBalance())
                                .build();
                emailService.sendEmailAlert(creditAlert);

                return BankResponse.builder()
                                .respondCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                                .accountInfo(null)
                                .build();
        }

        @Override
        public String getAccount(getAccountDto getAccount) {
            // Find the user by email using Optional
            Optional<User> optionalUser = userRepository.findByEmail(getAccount.getEmail());
        
            // Check if the user exists
            if (optionalUser.isEmpty()) {
                return "Account with the provided email doesn't exist";
            } else {
                // Get the user from the Optional
                User foundAccountUser = optionalUser.get();
                // Return the account number
                return "Your Account number is : " + foundAccountUser.getAccountNumber();
            }
        }
        

}
