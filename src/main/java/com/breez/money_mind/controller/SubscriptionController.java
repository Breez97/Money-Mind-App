package com.breez.money_mind.controller;

import com.breez.money_mind.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	@GetMapping("/subscriptions")
	public String getSubscriptions(Model model) {

		return "subscriptions";
	}

}
