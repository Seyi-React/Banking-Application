package com.oluwaseyi.Bank_Demo_Backend.utils;
import java.time.Year;




public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account created";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSSAGE ="Account successfully created";
    public static final String ACCOUNT_DO_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT__DO_NOT_EXIST_MESSSAGE ="User does not have an account ";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "This User has an account";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account successfully credited";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance ";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account debited successfully";
    public static final String USER_TO_DEBIT_CODE = "008";
    public static final String  USER_TO_DEBIT_CODE_MESSAGE = "Account to be credited does not exist";
    public static final String DESTINATION_ACCOUNT_DOES_NOT_EXIST_CODE = "009";
    public static final String DESTINATION_ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Destination account doesn't exist";
    public static final String SOURCE_ACCOUNT_DOES_NOT_EXIST_CODE = "010";
    public static final String SOURCE_ACCOUNT_DOES_NOT_EXIST_MESSAGE = "Source account number does not exist";
    
    



    public static String accountGenerator() {

         Year currentYear = Year.now();
         int min = 100000;
         int max  = 999999;

         int randNumber =(int) Math.floor( Math.random() * (max - min + 1) + min);

         String year = String.valueOf(currentYear);
         String randomNumber =  String.valueOf(randNumber);


         StringBuilder accountNumber = new StringBuilder();

         return accountNumber.append(year).append(randomNumber).toString();
    }
}





