package com.breez.money_mind.model.dto;

import com.breez.money_mind.validation.CustomDateValid;
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
	@CustomDateValid(message = "Invalid transaction date format, it can't be empty and use MM/dd/yyyy")
	@NotNull(message = "Transaction date can't be empty")
	private String transactionDate;
}
