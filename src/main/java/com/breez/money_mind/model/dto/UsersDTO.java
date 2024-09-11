package com.breez.money_mind.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDTO {

	private Integer id;
	@NotEmpty(message = "Name shouldn't be empty")
	private String name;
	@Size(min = 4, message = "Username should be longer than 4 symbols")
	private String username;
	@Size(min = 4, message = "Password should be longer than 4 symbols")
	private String password;
	private String newPassword;

}
