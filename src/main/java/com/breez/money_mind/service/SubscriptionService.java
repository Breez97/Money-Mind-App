package com.breez.money_mind.service;

import com.breez.money_mind.model.Subscription;
import com.breez.money_mind.model.UserPrincipal;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.SubscriptionDTO;
import com.breez.money_mind.repository.SubscriptionRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;
	@Autowired
	private UserRepository userRepository;

	public List<SubscriptionDTO> findAllSubscriptions() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated()) {
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			Users user = userRepository.findByUsername(userPrincipal.getUsername());
			List<Subscription> transactions = subscriptionRepository.findByUsersId(user.getId());
			return transactions.stream()
					.map(obj -> mapToSubscriptionDTO(obj))
					.collect(Collectors.toList());
		}
		throw new RuntimeException("User is not authenticated");
	}

	private SubscriptionDTO mapToSubscriptionDTO(Subscription subscription) {
		return SubscriptionDTO.builder()
				.id(subscription.getId())
				.title(subscription.getTitle())
				.amount(subscription.getAmount())
				.nextPayment(subscription.getNextPayment())
				.build();
	}

}
