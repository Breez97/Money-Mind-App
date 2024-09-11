package com.breez.money_mind.service;

import com.breez.money_mind.model.Transaction;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionService {

	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Autowired
	private TransactionRepository transactionRepository;

	public List<TransactionDTO> findAllTransactions(Users currentUser) {
		List<Transaction> transactions = transactionRepository.findByUserId(currentUser.getId());
		return transactions.stream()
				.map(obj -> mapToTransactionDTO(obj))
				.collect(Collectors.toList());
	}

	@Transactional
	public Map<String, String> addTransaction(TransactionDTO transactionDTO, Users currentUser) {
		Map<String, String> response = new HashMap<>();
		List<TransactionDTO> allTransactions = findAllTransactions(currentUser);

		if (checkTransactionExistenceInDB(transactionDTO, allTransactions)) {
			response.put("error", "Current transaction already exists");
			return response;
		}

		LocalDate parsedDate = LocalDate.parse(transactionDTO.getTransactionDate(), FORMATTER);
		LocalDate currentDate = LocalDate.now();
		if (!parsedDate.isBefore(currentDate)) {
			response.put("error", "Date must be in past");
			return response;
		}

		Transaction transaction = Transaction.builder()
				.title(transactionDTO.getTitle())
				.type(transactionDTO.getType())
				.amount(transactionDTO.getAmount())
				.category(transactionDTO.getCategory())
				.transactionDate(LocalDate.parse(transactionDTO.getTransactionDate(), FORMATTER))
				.user(currentUser)
				.build();
		try {
			transactionRepository.save(transaction);
			response.put("message", "Transaction saved successfully");
		} catch (Exception e) {
			response.put("error", "Error saving transaction: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	public Map<String, String> updateTransaction(TransactionDTO transactionDTO, Users currentUser) {
		Map<String, String> response = new HashMap<>();
		List<TransactionDTO> allTransactions = findAllTransactions(currentUser);
		if (!allTransactions.removeIf(transaction -> transaction.getId().equals(transactionDTO.getId()))) {
			throw new RuntimeException("Transaction not found");
		}

		if (checkTransactionExistenceInDB(transactionDTO, allTransactions)) {
			response.put("error", "Current transaction already exists");
			return response;
		}

		LocalDate parsedDate = LocalDate.parse(transactionDTO.getTransactionDate(), FORMATTER);
		LocalDate currentDate = LocalDate.now();
		if (!parsedDate.isBefore(currentDate)) {
			response.put("error", "Date must be in past");
			return response;
		}

		Transaction transaction = Transaction.builder()
				.id(transactionDTO.getId())
				.title(transactionDTO.getTitle())
				.type(transactionDTO.getType())
				.amount(transactionDTO.getAmount())
				.category(transactionDTO.getCategory())
				.transactionDate(LocalDate.parse(transactionDTO.getTransactionDate(), FORMATTER))
				.user(currentUser)
				.build();
		try {
			transactionRepository.save(transaction);
			response.put("message", "Transaction updated successfully");
		} catch (Exception e) {
			response.put("error", "Error saving transaction: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	public void deleteTransaction(Integer id) {
		transactionRepository.deleteById(id);
	}

	private boolean checkTransactionExistenceInDB(TransactionDTO transactionDTO, List<TransactionDTO> allTransactions) {
		for (TransactionDTO currentTransactionDTO : allTransactions) {
			if (Objects.equals(transactionDTO.getTitle(), currentTransactionDTO.getTitle())
					&& Objects.equals(transactionDTO.getType(), currentTransactionDTO.getType())
					&& Objects.equals(transactionDTO.getAmount(), currentTransactionDTO.getAmount())
					&& Objects.equals(transactionDTO.getCategory(), currentTransactionDTO.getCategory())
					&& Objects.equals(transactionDTO.getTransactionDate(), currentTransactionDTO.getTransactionDate())) {
				return true;
			}
		}
		return false;
	}

	public double sumByType(List<TransactionDTO> transactions, String type) {
		return transactions.stream()
				.filter(transaction -> transaction.getType().equals(type))
				.mapToDouble(transaction -> transaction.getAmount())
				.sum();
	}

	private TransactionDTO mapToTransactionDTO(Transaction transaction) {
		return TransactionDTO.builder()
				.id(transaction.getId())
				.title(transaction.getTitle())
				.type(transaction.getType())
				.amount(transaction.getAmount())
				.category(transaction.getCategory())
				.transactionDate(transaction.getTransactionDate().toString())
				.build();
	}

}
