package com.breez.money_mind.controller;

import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.service.TransactionService;
import com.breez.money_mind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private UserService userService;

	@GetMapping("/transactions")
	public String getTransactions(Model model) {
		Users currentUser = userService.getCurrentUser();
		List<TransactionDTO> transactions = transactionService.findAllTransactions(currentUser);
		double sumExpenses = Math.round(transactionService.sumByType(transactions, "expense") * 100.0) / 100.0;
		double sumIncomes = Math.round(transactionService.sumByType(transactions, "income") * 100.0) / 100.0;

		if (transactions.isEmpty()) {
			model.addAttribute("noTransactions", true);
		} else {
			model.addAttribute("noTransactions", false);
		}
		for (TransactionDTO transaction : transactions) {
			String formattedAmount = String.format("%.2f", transaction.getAmount());
			transaction.setFormattedAmount(formattedAmount);
		}
		model.addAttribute("transactions", transactions);
		model.addAttribute("sumExpenses", "-$" + String.format("%.2f", sumExpenses));
		model.addAttribute("sumIncomes", "+$" + String.format("%.2f", sumIncomes));

		return "transactions";
	}

	@PostMapping("/add-transaction")
	@ResponseBody
	public ResponseEntity<?> addTransaction(@Valid @ModelAttribute("transaction") TransactionDTO transactionDTO,
											BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage())
			);
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			Users user = userService.getCurrentUser();
			String result = transactionService.addTransaction(transactionDTO, user);

			String errorDate = "Date must be in past";
			if (result.equals(errorDate)) {
				Map<String, String> errors = new HashMap<>();
				errors.put("error", errorDate);
				return ResponseEntity.badRequest().body(errors);
			}

			String errorUpdate = "Current transaction already exists";
			if (result.equals(errorUpdate)) {
				Map<String, String> errors = new HashMap<>();
				errors.put("error", errorUpdate);
				return ResponseEntity.badRequest().body(errors);
			}

			return ResponseEntity.ok(result);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@PostMapping("/update-transaction")
	@ResponseBody
	public ResponseEntity<?> updateTransaction(@Valid @ModelAttribute("transactionDTO") TransactionDTO transactionDTO,
											   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage())
			);
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			Users user = userService.getCurrentUser();
			String result = transactionService.updateTransaction(transactionDTO, user);
			
			String errorDate = "Date must be in past";
			if (result.equals(errorDate)) {
				Map<String, String> errors = new HashMap<>();
				errors.put("error", errorDate);
				return ResponseEntity.badRequest().body(errors);
			}

			String errorUpdate = "Current transaction already exists";
			if (result.equals(errorUpdate)) {
				Map<String, String> errors = new HashMap<>();
				errors.put("error", errorUpdate);
				return ResponseEntity.badRequest().body(errors);
			}
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/delete-transaction/{id}")
	public ResponseEntity<Map<String, String>> deleteTransaction(@PathVariable("id") Integer id) {
		try {
			transactionService.deleteTransaction(id);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Transaction deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Failed to delete transaction: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
