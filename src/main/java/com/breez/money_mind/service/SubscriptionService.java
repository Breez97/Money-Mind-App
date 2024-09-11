package com.breez.money_mind.service;

import com.breez.money_mind.model.Subscription;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.SubscriptionDTO;
import com.breez.money_mind.repository.SubscriptionRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
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
public class SubscriptionService {

	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Autowired
	private SubscriptionRepository subscriptionRepository;
	@Autowired
	private UserRepository userRepository;

	public List<SubscriptionDTO> findAllSubscriptions(Users currentUser) {
		List<Subscription> transactions = subscriptionRepository.findByUserId(currentUser.getId());
		return transactions.stream()
				.map(obj -> mapToSubscriptionDTO(obj))
				.collect(Collectors.toList());
	}

	@Transactional
	public Map<String, String> addSubscription(SubscriptionDTO subscriptionDTO, Users currentUser) {
		Map<String, String> response = new HashMap<>();
		List<SubscriptionDTO> allSubscriptions = findAllSubscriptions(currentUser);

		if (checkSubscriptionExistenceIdDB(subscriptionDTO, allSubscriptions)) {
			response.put("error", "Current subscription already exists");
			return response;
		}

		LocalDate parsedDate = LocalDate.parse(subscriptionDTO.getNextPayment(), FORMATTER);
		LocalDate nextDay = LocalDate.now().plusDays(1);
		if (parsedDate.isBefore(nextDay)) {
			response.put("error", "Date must be in future (at least the next day)");
			return response;
		}

		Subscription subscription = Subscription.builder()
				.title(subscriptionDTO.getTitle())
				.amount(subscriptionDTO.getAmount())
				.frequency(subscriptionDTO.getFrequency())
				.nextPayment(parsedDate)
				.user(currentUser)
				.build();
		try {
			subscriptionRepository.save(subscription);
			response.put("message", "Subscription saved successfully");
		} catch (Exception e) {
			response.put("error", "Error saving subscription: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	public Map<String, String> updateSubscription(SubscriptionDTO subscriptionDTO, Users currentUser) {
		Map<String, String> response = new HashMap<>();
		List<SubscriptionDTO> allSubscriptions = findAllSubscriptions(currentUser);
		if (!allSubscriptions.removeIf(subscription -> subscription.getId().equals(subscriptionDTO.getId()))) {
			throw new RuntimeException("Subscription not found");
		}

		if (checkSubscriptionExistenceIdDB(subscriptionDTO, allSubscriptions)) {
			response.put("error", "Current subscription already exists");
			return response;
		}

		LocalDate parsedDate = LocalDate.parse(subscriptionDTO.getNextPayment(), FORMATTER);
		LocalDate nextDay = LocalDate.now().plusDays(1);
		if (parsedDate.isBefore(nextDay)) {
			response.put("error", "Date must be in future (at least the next day)");
			return response;
		}

		Subscription subscription = Subscription.builder()
				.id(subscriptionDTO.getId())
				.title(subscriptionDTO.getTitle())
				.amount(subscriptionDTO.getAmount())
				.frequency(subscriptionDTO.getFrequency())
				.nextPayment(parsedDate)
				.user(currentUser)
				.build();
		try {
			subscriptionRepository.save(subscription);
			response.put("message", "Subscription updated successfully");
		} catch (Exception e) {
			response.put("error", "Error saving subscription: " + e.getMessage());
		}
		return response;
	}

	@Transactional
	public void deleteSubscription(Integer id) {
		subscriptionRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Subscription> getSubscriptionsForTomorrow() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		return subscriptionRepository.findByNextPayment(tomorrow);
	}

	private boolean checkSubscriptionExistenceIdDB(SubscriptionDTO subscriptionDTO, List<SubscriptionDTO> allSubscriptions) {
		for (SubscriptionDTO currentSubscriptionDTO : allSubscriptions) {
			if (Objects.equals(subscriptionDTO.getTitle(), currentSubscriptionDTO.getTitle())
					&& Objects.equals(subscriptionDTO.getAmount(), currentSubscriptionDTO.getAmount())
					&& Objects.equals(subscriptionDTO.getFrequency(), currentSubscriptionDTO.getFrequency())
					&& Objects.equals(subscriptionDTO.getNextPayment(), currentSubscriptionDTO.getNextPayment())) {
				return true;
			}
		}
		return false;
	}

	private SubscriptionDTO mapToSubscriptionDTO(Subscription subscription) {
		return SubscriptionDTO.builder()
				.id(subscription.getId())
				.title(subscription.getTitle())
				.amount(subscription.getAmount())
				.frequency(subscription.getFrequency())
				.nextPayment(subscription.getNextPayment().toString())
				.build();
	}

	@Scheduled(cron = "0 00 00 * * ?")
	@Transactional
	public void updateNextPayment() {
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		LocalDate today = LocalDate.now();
		for (Subscription subscription : subscriptions) {
			LocalDate newNextPayment = calculateNextPaymentDate(subscription.getNextPayment(), subscription.getFrequency(), today);
			subscription.setNextPayment(newNextPayment);
		}
		subscriptionRepository.saveAll(subscriptions);
	}

	private LocalDate calculateNextPaymentDate(LocalDate currentNextPayment, String frequency, LocalDate today) {
		if (frequency == null || currentNextPayment == null) {
			return currentNextPayment;
		}
		switch (frequency) {
			case "Daily":
				return currentNextPayment.plusDays(1);
			case "Weekly":
				return currentNextPayment.plusWeeks(1);
			case "Monthly":
				return currentNextPayment.plusMonths(1);
			case "Annually":
				return currentNextPayment.plusYears(1);
			default:
				return currentNextPayment;
		}
	}

}
