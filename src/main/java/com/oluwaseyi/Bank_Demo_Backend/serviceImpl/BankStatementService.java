
package com.oluwaseyi.Bank_Demo_Backend.serviceImpl;

import java.io.ByteArrayOutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import com.oluwaseyi.Bank_Demo_Backend.entity.Transaction;
import com.oluwaseyi.Bank_Demo_Backend.entity.User;
import com.oluwaseyi.Bank_Demo_Backend.repository.TransactionRepository;
import com.oluwaseyi.Bank_Demo_Backend.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatementService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> {
                    LocalDateTime createdAt = transaction.getCreatedAt();
                    return createdAt != null && !createdAt.toLocalDate().isBefore(start) && !createdAt.toLocalDate().isAfter(end);
                })
                .collect(Collectors.toList());
    }

    public byte[] generateStatementPdf(String accountNumber, String startDate, String endDate) {
        List<Transaction> transactions = generateStatement(accountNumber, startDate, endDate);
        User userInfo = userRepository.findByAccountNumber(accountNumber);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Adding Bank Info
            PdfPTable bankInfoTable = new PdfPTable(1);
            bankInfoTable.setWidthPercentage(100);
            PdfPCell bankNameCell = new PdfPCell(new Phrase("Oluwaseyi Banking Statement", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
            bankNameCell.setBorder(PdfPCell.NO_BORDER);
            bankNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            bankInfoTable.addCell(bankNameCell);

            PdfPCell addressCell = new PdfPCell(new Phrase("Block 2a, Some Address, Lagos, Nigeria"));
            addressCell.setBorder(PdfPCell.NO_BORDER);
            addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            bankInfoTable.addCell(addressCell);

            document.add(bankInfoTable);
            document.add(new Paragraph(" ")); // Add some spacing

            // Adding Customer Info
            PdfPTable customerInfoTable = new PdfPTable(2);
            customerInfoTable.setWidthPercentage(100);
            customerInfoTable.addCell(createCell("Start Date: " + startDate, PdfPCell.NO_BORDER));
            customerInfoTable.addCell(createCell("End Date: " + endDate, PdfPCell.NO_BORDER));
            customerInfoTable.addCell(createCell("Customer Name: " + userInfo.getFirstName() + " " + userInfo.getLastName(), PdfPCell.NO_BORDER));
            customerInfoTable.addCell(createCell("Customer Address: " + userInfo.getAddress(), PdfPCell.NO_BORDER));

            document.add(customerInfoTable);
            document.add(new Paragraph(" ")); // Add some spacing

            // Adding Transaction Info
            PdfPTable transactionTable = new PdfPTable(4);
            transactionTable.setWidthPercentage(100);
            transactionTable.addCell(createHeaderCell("DATE"));
            transactionTable.addCell(createHeaderCell("TRANSACTION TYPE"));
            transactionTable.addCell(createHeaderCell("TRANSACTION AMOUNT"));
            transactionTable.addCell(createHeaderCell("STATUS"));

            for (Transaction transaction : transactions) {
                transactionTable.addCell(createCell(transaction.getCreatedAt().toLocalDate().toString(), PdfPCell.BOX));
                transactionTable.addCell(createCell(transaction.getTransactionType(), PdfPCell.BOX));
                transactionTable.addCell(createCell(transaction.getAmount().toString(), PdfPCell.BOX));
                transactionTable.addCell(createCell(transaction.getStatus(), PdfPCell.BOX));
            }

            document.add(transactionTable);
            document.close();

        } catch (Exception e) {
            log.error("Error generating PDF", e);
        }

        return outputStream.toByteArray();
    }

    private PdfPCell createCell(String content, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBorder(border);
        return cell;
    }

    private PdfPCell createHeaderCell(String content) {
        PdfPCell cell = createCell(content, PdfPCell.BOX);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}
