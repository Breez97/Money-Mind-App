package com.breez.money_mind.controller;

import com.breez.money_mind.model.dto.UsersDTO;
import com.breez.money_mind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
	@ResponseBody
	public ResponseEntity<?> registerNewUser(@Valid @ModelAttribute("userDTO") UsersDTO usersDTO,
											 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage())
			);
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			String result = userService.saveUser(usersDTO);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

}
