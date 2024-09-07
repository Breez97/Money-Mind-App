package com.breez.money_mind.controller;

import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/transactions")
	public String getTransactions(Model model) {
		List<TransactionDTO> transactions = transactionService.findAllTransactions();
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
			System.out.println(errors);
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			String result = transactionService.addTransaction(transactionDTO);
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
			String result = transactionService.updateTransaction(transactionDTO);
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

	private ResponseEntity<?> errorHandler(BindingResult bindingResult) {
		Map<String, String> errors = new HashMap<>();
		bindingResult.getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage())
		);
		return ResponseEntity.badRequest().body(errors);
	}

	@GetMapping("/delete-transaction/{id}")
	public String deleteTransaction(@PathVariable("id") Integer id,
									RedirectAttributes redirectAttributes) {
		String result = transactionService.deleteTransaction(id);
		redirectAttributes.addFlashAttribute("message", result);

		return "redirect:/transactions";
	}

}
