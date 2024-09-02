package com.breez.money_mind.controller;

import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/transactions")
	public String getTransactions(Model model) {
		List<TransactionDTO> transactions = transactionService.findAllTransactions();
		double sumExpenses = transactionService.sumByType(transactions, "expense");
		double sumIncomes = transactionService.sumByType(transactions, "income");
		model.addAttribute("transactions", transactions);
		model.addAttribute("sumExpenses", "$" + sumExpenses);
		model.addAttribute("sumIncomes", "$" + sumIncomes);
		return "transactions";
	}

}
