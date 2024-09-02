package com.breez.money_mind.service;

import com.breez.money_mind.model.Transaction;
import com.breez.money_mind.model.UserPrincipal;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.repository.TransactionRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private UserRepository userRepository;

	public List<TransactionDTO> findAllTransactions() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated()) {
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			String username = userPrincipal.getUsername();
			Users user = userRepository.findByUsername(username);
			int userId = user.getId();
			List<Transaction> transactions = transactionRepository.findByUsersId(userId);
			return transactions.stream()
					.map(obj -> convertToDTO(obj))
					.collect(Collectors.toList());
		}
		throw new RuntimeException("User is not authenticated");
	}

	public double sumByType(List<TransactionDTO> transactions, String type) {
		return transactions.stream()
				.filter(transaction -> transaction.getType().equals(type))
				.mapToDouble(transaction -> transaction.getAmount())
				.sum();
	}

	private TransactionDTO convertToDTO(Transaction transaction) {
		return TransactionDTO.builder()
				.id(transaction.getId())
				.title(transaction.getTitle())
				.type(transaction.getType())
				.amount(transaction.getAmount())
				.category(transaction.getCategory())
				.transactionDate(transaction.getTransactionDate())
				.build();
	}

}
