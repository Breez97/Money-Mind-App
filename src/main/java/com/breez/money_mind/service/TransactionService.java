package com.breez.money_mind.service;

import com.breez.money_mind.exceptions.TransactionNotFoundException;
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
import java.util.Objects;
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
			Users user = userRepository.findByUsername(userPrincipal.getUsername());
			List<Transaction> transactions = transactionRepository.findByUsersId(user.getId());
			return transactions.stream()
					.map(obj -> mapToTransactionDTO(obj))
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

	public String updateTransactionById(TransactionDTO transactionDTO) {
		List<TransactionDTO> allTransactions = findAllTransactions();
		if (!allTransactions.removeIf(transaction -> transaction.getId().equals(transactionDTO.getId()))) {
			return "Transaction not found";
		}
		for (TransactionDTO currentTransactionDTO : allTransactions) {
			if (Objects.equals(transactionDTO.getTitle(), currentTransactionDTO.getTitle())
					&& Objects.equals(transactionDTO.getType(), currentTransactionDTO.getType())
					&& Objects.equals(transactionDTO.getAmount(), currentTransactionDTO.getAmount())
					&& Objects.equals(transactionDTO.getCategory(), currentTransactionDTO.getCategory())
					&& Objects.equals(transactionDTO.getTransactionDate(), currentTransactionDTO.getTransactionDate())) {
				return "Current transaction already exists";
			}
		}
		Transaction transaction = mapToTransaction(transactionDTO);
		try {
			transactionRepository.save(transaction);
			return "Transaction updated successfully";
		} catch (Exception e) {
			return "Error updating transaction: " + e.getMessage();
		}
	}

	private TransactionDTO mapToTransactionDTO(Transaction transaction) {
		return TransactionDTO.builder()
				.id(transaction.getId())
				.title(transaction.getTitle())
				.type(transaction.getType())
				.amount(transaction.getAmount())
				.category(transaction.getCategory())
				.transactionDate(transaction.getTransactionDate())
				.build();
	}

	private Transaction mapToTransaction(TransactionDTO transactionDTO) {
		return Transaction.builder()
				.id(transactionDTO.getId())
				.title(transactionDTO.getTitle())
				.type(transactionDTO.getType())
				.amount(transactionDTO.getAmount())
				.category(transactionDTO.getCategory())
				.transactionDate(transactionDTO.getTransactionDate())
				.build();
	}
}
