package com.breez.money_mind.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDTO {

	private Integer id;
	private String username;
	private String password;
	private String name;

}
