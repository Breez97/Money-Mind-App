package com.breez.money_mind.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {

	private Integer id;
	private Long chatId;
	private Boolean telegramEnabled;

}
