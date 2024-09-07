package com.breez.money_mind.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionDTO {

	private Integer id;
	@NotEmpty(message = "Transaction title can't be empty")
	private String title;
	@NotNull(message = "Transaction type can't be empty")
	private String type;
	@PositiveOrZero(message = "Amount must be positive")
	@NotNull(message = "Transaction amount is required")
	private Double amount;
	private String formattedAmount;
	@NotEmpty(message = "Transaction category can't be empty")
	private String category;
	@NotNull(message = "Transaction date can't be empty")
	@PastOrPresent(message = "Transaction date can't be in the future")
	private LocalDate transactionDate;
}
