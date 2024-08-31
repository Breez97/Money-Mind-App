package com.breez.money_mind.controller;

import com.breez.money_mind.model.Users;
import com.breez.money_mind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public String register(@ModelAttribute Users user) {
		userService.registerUser(user);
		return "redirect:/login-page";
	}

}
