package com.breez.money_mind.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	// expense, income
	private String type;
	private double amount;
	// expense: supermarkets, clothes, fast_food, transaction, others
	// income: transaction, atm, others
	private String category;
	private LocalDate transactionDate;

	@ManyToOne
	@JoinTable(
			name = "user_transactions",
			joinColumns = @JoinColumn(name = "transaction_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Users users;

}
