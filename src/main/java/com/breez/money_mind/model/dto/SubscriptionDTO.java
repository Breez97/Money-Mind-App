package com.breez.money_mind.model.dto;

import com.breez.money_mind.validation.CustomDateValid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

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
	@CustomDateValid(message = "Invalid subscription date format, it can't be empty and use MM/dd/yyyy")
	@NotNull(message = "Subscription date can't be empty")
	private String nextPayment;

}
