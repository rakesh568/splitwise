package com.rakesh.splitwise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private LocalDate date;
    private String group;
    private String expense;
    private double totalAmount;
    private String share; // "+400" or "-25"
}
