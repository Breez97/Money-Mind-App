package com.breez.money_mind.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SubscriptionDTO {

	private Integer id;
	@NotEmpty(message = "Subscription title shouldn't be empty")
	private String title;
	@PositiveOrZero(message = "Amount must be positive")
	@NotNull(message = "Transaction amount is required")
	private Double amount;
	private String formattedAmount;
	@NotEmpty(message = "Subscription frequency shouldn't be empty")
	private String frequency;
	@NotNull(message = "Subscription date can't be empty")
	@Future(message = "Subscription date should be in future")
	private LocalDate nextPayment;

}
