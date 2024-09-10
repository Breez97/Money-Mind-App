package com.breez.money_mind.service;

import com.breez.money_mind.exceptions.TransactionNotFoundException;
import com.breez.money_mind.model.Subscription;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.SubscriptionDTO;
import com.breez.money_mind.repository.SubscriptionRepository;
import com.breez.money_mind.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

	public String addSubscription(SubscriptionDTO subscriptionDTO, Users currentUser) {
		List<SubscriptionDTO> allSubscriptions = findAllSubscriptions(currentUser);
		if (checkSubscriptionExistenceIdDB(subscriptionDTO, allSubscriptions)) {
			return "Current subscription already exists";
		}

		LocalDate parsedDate = LocalDate.parse(subscriptionDTO.getNextPayment(), FORMATTER);
		LocalDate nextDay = LocalDate.now().plusDays(1);
		if (parsedDate.isBefore(nextDay)) {
			return "Date must be in future (at least the next day)";
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
			return "Subscription saves successfully";
		} catch (Exception e) {
			return "Error saving subscription: " + e.getMessage();
		}
	}

	public String updateSubscription(@Valid SubscriptionDTO subscriptionDTO, Users currentUser) {
		List<SubscriptionDTO> allSubscriptions = findAllSubscriptions(currentUser);
		if (!allSubscriptions.removeIf(subscription -> subscription.getId().equals(subscriptionDTO.getId()))) {
			throw new TransactionNotFoundException("Subscription not found");
		}
		if (checkSubscriptionExistenceIdDB(subscriptionDTO, allSubscriptions)) {
			return "Current subscription already exists";
		}

		LocalDate parsedDate = LocalDate.parse(subscriptionDTO.getNextPayment(), FORMATTER);
		LocalDate nextDay = LocalDate.now().plusDays(1);
		if (parsedDate.isBefore(nextDay)) {
			return "Date must be in future (at least the next day)";
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
			return "Transaction updated successfully";
		} catch (Exception e) {
			return "Error updating transaction: " + e.getMessage();
		}
	}

	public void deleteSubscription(Integer id) {
		subscriptionRepository.deleteById(id);
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
}
