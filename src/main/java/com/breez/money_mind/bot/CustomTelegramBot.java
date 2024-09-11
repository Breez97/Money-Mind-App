package com.breez.money_mind.bot;

import com.breez.money_mind.model.BotModel;
import com.breez.money_mind.repository.UserRepository;
import com.breez.money_mind.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class CustomTelegramBot extends TelegramLongPollingBot {

	private final BotModel botModel;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NotificationService notificationService;

	@Override
	public String getBotUsername() {
		return botModel.getBotName();
	}

	@Override
	public String getBotToken() {
		return botModel.getToken();
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			if (messageText.startsWith("/start")) {
				String userName = update.getMessage().getChat().getFirstName();
				startCommandReceived(chatId, userName);
			} else if (messageText.startsWith("@")) {
				saveUserNickname(chatId, messageText.substring(1));
			} else {
				sendNotification(chatId, "Excuse me, I can understand only /start command or send your nickname starting with '@'.");
			}
		}
	}

	private void startCommandReceived(Long chatId, String name) {
		String answer = "Hello " + name + "!" + "\n" + "Nice to meet you!" + "\n\n"
				+ "I can help you to organize your subscriptions" + "\n"
				+ "I'll send you a message one day before the money will be debited from your account for certain subscription";

		sendNotification(chatId, answer);

		String requestNickname = "Please send me your nickname on the platform (start with '@').";
		sendNotification(chatId, requestNickname);
	}

	private void saveUserNickname(Long chatId, String username) {
		if (userRepository.findByUsername(username) != null) {
			String confirmation = "Thank you! Your nickname " + username + " has been saved.";
			notificationService.saveChatIdByUsername(username, chatId);
			sendNotification(chatId, confirmation);
		} else {
			String errorMessage = "Sorry, the nickname " + username + " does not exist on the platform.";
			sendNotification(chatId, errorMessage);
		}
	}

	public void sendNotification(long chatId, String textToSend) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));
		sendMessage.setText(textToSend);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
