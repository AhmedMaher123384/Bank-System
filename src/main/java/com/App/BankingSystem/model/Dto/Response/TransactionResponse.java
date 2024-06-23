package com.App.BankingSystem.model.Dto.Response;

import com.App.BankingSystem.model.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private Double amount;
    private Double balance;
    private TransactionType type;
    private Date timestamp;
    private String notes;
}