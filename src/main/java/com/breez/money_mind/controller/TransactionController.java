package com.breez.money_mind.controller;

import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/transactions")
	public String getTransactions(Model model) {
		List<TransactionDTO> transactions = transactionService.findAllTransactions();
		double sumExpenses = Math.round(transactionService.sumByType(transactions, "expense") * 100.0) / 100.0;
		double sumIncomes = Math.round(transactionService.sumByType(transactions, "income") * 100.0) / 100.0;

		model.addAttribute("transactions", transactions);
		model.addAttribute("sumExpenses", "-$" + sumExpenses);
		model.addAttribute("sumIncomes", "+$" + sumIncomes);

		return "transactions";
	}

	@PostMapping("/add-transaction")
	public String addTransaction(@RequestParam("title") String title,
								 @RequestParam("type") String type,
								 @RequestParam("amount") double amount,
								 @RequestParam("category") String category,
								 @RequestParam("transactionDate") LocalDate transactionDate,
								 RedirectAttributes redirectAttributes) {
		TransactionDTO transactionDTO = TransactionDTO.builder()
				.title(title)
				.type(type)
				.amount(amount)
				.category(category)
				.transactionDate(transactionDate)
				.build();

		String result = transactionService.addTransaction(transactionDTO);
		redirectAttributes.addFlashAttribute("message", result);

		return "redirect:/transactions";
	}

	@PostMapping("/update-transaction")
	public String updateTransaction(@RequestParam("id") Integer id,
									@RequestParam("title") String title,
									@RequestParam("type") String type,
									@RequestParam("amount") double amount,
									@RequestParam("category") String category,
									@RequestParam("transactionDate") LocalDate transactionDate,
									RedirectAttributes redirectAttributes) {
		TransactionDTO transactionDTO = TransactionDTO.builder()
				.id(id)
				.title(title)
				.type(type)
				.amount(amount)
				.category(category)
				.transactionDate(transactionDate)
				.build();

		String result = transactionService.updateTransaction(transactionDTO);
		redirectAttributes.addFlashAttribute("message", result);

		return "redirect:/transactions";
	}

	@GetMapping("/delete-transaction/{id}")
	public String deleteTransaction(@PathVariable("id") Integer id,
									RedirectAttributes redirectAttributes) {
		String result = transactionService.deleteTransaction(id);
		redirectAttributes.addFlashAttribute("message", result);

		return "redirect:/transactions";
	}

}
