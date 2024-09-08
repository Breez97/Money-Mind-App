package com.breez.money_mind.service;

import com.breez.money_mind.model.Notification;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.NotificationDTO;
import com.breez.money_mind.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public NotificationDTO getInfoAboutNotifications(Users currentUser) {
		Notification notification = notificationRepository.findByUserId(currentUser.getId());
		return mapToNotificationDTO(notification);
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
