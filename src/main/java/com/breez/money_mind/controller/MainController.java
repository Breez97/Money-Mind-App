package com.breez.money_mind.controller;

import com.breez.money_mind.exceptions.UserAlreadyExistsException;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.UsersDTO;
import com.breez.money_mind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MainController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String indexPage(Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("username", username);
		} else {
			model.addAttribute("username", "Guest");
		}
		return "index";
	}

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
