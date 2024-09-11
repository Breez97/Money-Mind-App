package com.breez.money_mind.bot;

import com.breez.money_mind.model.Notification;
import com.breez.money_mind.model.Subscription;
import com.breez.money_mind.service.NotificationService;
import com.breez.money_mind.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class NotificationScheduler {

	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private CustomTelegramBot telegramBot;

	@Scheduled(cron = "0 00 12 * * ?")
	@Transactional(readOnly = true)
	public void sendNotifications() {
		List<Subscription> subscriptions = subscriptionService.getSubscriptionsForTomorrow();
		for (Subscription subscription : subscriptions) {
			Notification notification = notificationService.getNotificationByUserId(subscription.getUser().getId());
			if (notification != null && notification.getTelegramEnabled()) {
				String message = "Reminder:" + "\n" + "Tomorrow there will be a payment of $" + subscription.getAmount()
						+ " for " + subscription.getTitle();
				telegramBot.sendNotification(notification.getChatId(), message);
			}
		}
	}
}