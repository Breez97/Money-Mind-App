package com.breez.money_mind.controller;

import com.breez.money_mind.exceptions.UserAlreadyExistsException;
import com.breez.money_mind.model.dto.UsersDTO;
import com.breez.money_mind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/login-page")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
							@RequestParam(value = "message", required = false) String errorMessage,
							Model model) {
		if (error != null) {
			model.addAttribute("errorMessage", errorMessage);
		}

		return "login-page";
	}

	@PostMapping("/login")
	public String processLogin() {
		return "redirect:/";
	}

	@GetMapping("/register-page")
	public String registerPage() {
		return "register-page";
	}

	@PostMapping("/register")
	public String processRegister(@ModelAttribute UsersDTO userDTO, Model model) {
		try {
			userService.saveUser(userDTO);
			return "login-page";
		} catch (UserAlreadyExistsException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "register-page";
		}
	}

}
