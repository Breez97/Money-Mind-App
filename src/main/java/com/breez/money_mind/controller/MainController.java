package com.breez.money_mind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class MainController {

	@Autowired
	private AuthenticationManager authenticationManager;

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
}
