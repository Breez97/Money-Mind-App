package com.breez.money_mind.service;

import com.breez.money_mind.model.Notification;
import com.breez.money_mind.model.UserPrincipal;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.NotificationDTO;
import com.breez.money_mind.repository.NotificationRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private UserRepository userRepository;

	public NotificationDTO getInfoAboutNotifications() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.isAuthenticated()) {
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			Users user = userRepository.findByUsername(userPrincipal.getUsername());
			Notification notification = notificationRepository.findByUserId(user.getId());
			return mapToNotificationDTO(notification);
		}
		throw new RuntimeException("User is not authenticated");
	}

	private NotificationDTO mapToNotificationDTO(Notification notification) {
		return NotificationDTO.builder()
				.id(notification.getId())
				.telegram(notification.getTelegram())
				.telegramEnabled(notification.getTelegramEnabled())
				.email(notification.getEmail())
				.emailEnabled(notification.getEmailEnabled())
				.build();
	}

}
