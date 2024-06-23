package com.App.BankingSystem.model.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransferResponse {
    private Long transactionId;
    private Double amount;
    private Double balance;
    private String status;
    private Date timestamp;
    private String message;
}