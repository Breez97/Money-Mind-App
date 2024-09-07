package com.breez.money_mind.controller;

import com.breez.money_mind.model.dto.NotificationDTO;
import com.breez.money_mind.model.dto.SubscriptionDTO;
import com.breez.money_mind.model.dto.TransactionDTO;
import com.breez.money_mind.service.NotificationService;
import com.breez.money_mind.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private NotificationService notificationService;

	@GetMapping("/subscriptions")
	public String getSubscriptions(Model model) {
		List<SubscriptionDTO> subscriptions = subscriptionService.findAllSubscriptions();
		NotificationDTO notification = notificationService.getInfoAboutNotifications();

		if (subscriptions.isEmpty()) {
			model.addAttribute("noSubscriptions", true);
		} else {
			model.addAttribute("noSubscriptions", false);
		}
		for (SubscriptionDTO subscription : subscriptions) {
			String formattedAmount = String.format("%.2f", subscription.getAmount());
			subscription.setFormattedAmount(formattedAmount);
		}

		model.addAttribute("subscriptions", subscriptions);
		model.addAttribute("notification", notification);
		return "subscriptions";
	}

}
