package com.oluwaseyi.Bank_Demo_Backend.serviceImpl;

import com.oluwaseyi.Bank_Demo_Backend.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
