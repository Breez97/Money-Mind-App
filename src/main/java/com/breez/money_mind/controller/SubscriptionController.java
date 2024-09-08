package com.breez.money_mind.controller;

import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.NotificationDTO;
import com.breez.money_mind.model.dto.SubscriptionDTO;
import com.breez.money_mind.service.NotificationService;
import com.breez.money_mind.service.SubscriptionService;
import com.breez.money_mind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private UserService userService;

	@GetMapping("/subscriptions")
	public String getSubscriptions(Model model) {
		Users currentUser = userService.getCurrentUser();
		List<SubscriptionDTO> subscriptions = subscriptionService.findAllSubscriptions(currentUser);
		NotificationDTO notification = notificationService.getInfoAboutNotifications(currentUser);

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

	@PostMapping("/add-subscription")
	@ResponseBody
	public ResponseEntity<?> addSubscription(@Valid @ModelAttribute("subscription") SubscriptionDTO subscriptionDTO,
											 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			Users user = userService.getCurrentUser();
			String result = subscriptionService.addSubscription(subscriptionDTO, user);
			String errorAdd = "Current subscription already exists";
			if (result.equals(errorAdd)) {
				Map<String, String> errors = new HashMap<>();
				errors.put("error", errorAdd);
				return ResponseEntity.badRequest().body(errors);
			}
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

}
