package com.breez.money_mind.controller;

import com.breez.money_mind.model.Notification;
import com.breez.money_mind.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;

@Controller
public class NotificationsController {

	@Autowired
	private NotificationService notificationService;

	@PostMapping("/notification-enable-update")
	public ResponseEntity<?> updateNotification(@RequestBody Notification request) {
		boolean updated = notificationService.updateNotificationTelegram(request.getTelegramEnabled());
		if (updated) {
			return ResponseEntity.ok().body(Collections.singletonMap("success", true));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("success", false));
		}
	}

}
