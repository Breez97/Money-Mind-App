package com.breez.money_mind.service;

import com.breez.money_mind.model.Subscription;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.SubscriptionDTO;
import com.breez.money_mind.repository.SubscriptionRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

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

		Subscription subscription = Subscription.builder()
				.title(subscriptionDTO.getTitle())
				.amount(subscriptionDTO.getAmount())
				.frequency(subscriptionDTO.getFrequency())
				.nextPayment(subscriptionDTO.getNextPayment())
				.user(currentUser)
				.build();
		try {
			subscriptionRepository.save(subscription);
			return "Subscription saves successfully";
		} catch (Exception e) {
			return "Error saving subscription: " + e.getMessage();
		}
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
				.nextPayment(subscription.getNextPayment())
				.build();
	}
}
