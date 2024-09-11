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
import org.springframework.web.bind.annotation.*;

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
			Map<String, String> response = subscriptionService.addSubscription(subscriptionDTO, user);
			if (response.containsKey("error")) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@PostMapping("/update-subscription")
	public ResponseEntity<?> updateSubscription(@Valid @ModelAttribute("subscription") SubscriptionDTO subscriptionDTO,
												BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			Users user = userService.getCurrentUser();
			Map<String, String> response = subscriptionService.updateSubscription(subscriptionDTO, user);
			if (response.containsKey("error")) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/delete-subscription/{id}")
	public ResponseEntity<Map<String, String>> deleteSubscription(@PathVariable("id") Integer id) {
		try {
			subscriptionService.deleteSubscription(id);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Subscription deleted successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, String> response = new HashMap<>();
			response.put("error", "Failed to delete subscription: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
