package com.breez.money_mind.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TransactionDTO {

	private Integer id;
	private String title;
	private String type;
	private double amount;
	private String category;
	private LocalDate transactionDate;

}
