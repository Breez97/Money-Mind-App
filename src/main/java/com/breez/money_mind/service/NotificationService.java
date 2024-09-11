package com.breez.money_mind.service;

import com.breez.money_mind.model.Notification;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.NotificationDTO;
import com.breez.money_mind.repository.NotificationRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	public NotificationDTO getInfoAboutNotifications(Users currentUser) {
		Notification notification = notificationRepository.findByUserId(currentUser.getId());
		return mapToNotificationDTO(notification);
	}

	private NotificationDTO mapToNotificationDTO(Notification notification) {
		return NotificationDTO.builder()
				.id(notification.getId())
				.telegramEnabled(notification.getTelegramEnabled())
				.chatId(notification.getChatId())
				.build();
	}

	@Transactional
	public void saveChatIdByUsername(String username, Long chatId) {
		Users user = userRepository.findByUsername(username);
		Notification notificationByUserId = notificationRepository.findByUserId(user.getId());
		Notification notification = Notification.builder()
				.id(notificationByUserId.getId())
				.chatId(chatId)
				.telegramEnabled(notificationByUserId.getTelegramEnabled())
				.build();
		try {
			notificationRepository.save(notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional(readOnly = true)
	public Notification getNotificationByUserId(Integer userId) {
		return notificationRepository.findByUserId(userId);
	}

	public boolean updateNotificationTelegram(Boolean telegramEnabled) {
		Users user = userService.getCurrentUser();
		Notification currentNotification = notificationRepository.findByUserId(user.getId());
		Notification newNotification = Notification.builder()
				.id(currentNotification.getId())
				.chatId(currentNotification.getChatId())
				.telegramEnabled(telegramEnabled)
				.user(user)
				.build();
		try {
			notificationRepository.save(newNotification);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
