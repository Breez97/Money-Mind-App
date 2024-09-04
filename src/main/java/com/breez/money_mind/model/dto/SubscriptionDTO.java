package com.breez.money_mind.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SubscriptionDTO {

	private Integer id;
	private String title;
	private double amount;
	private LocalDate nextPayment;

}
