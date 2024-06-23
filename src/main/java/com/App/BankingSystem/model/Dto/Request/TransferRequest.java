package com.App.BankingSystem.model.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransferRequest {
    private String sourceCardNumber;
    private String destinationCardNumber;
    private String cvv;
    private double amount;
}
