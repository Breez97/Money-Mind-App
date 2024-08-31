package com.breez.money_mind.controller;

import com.breez.money_mind.model.Users;
import com.breez.money_mind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		return userService.registerUser(user);
	}

	@PostMapping("/login")
	public String login(@RequestBody Users user) {
		return userService.verify(user);
	}

}
