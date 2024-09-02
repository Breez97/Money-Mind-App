package com.breez.money_mind.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

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

}
