package com.devbc.banking.dto;

public record TransferFundDto(Long fromAccountId, Long toAccountId, double amount) {
}
