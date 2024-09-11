package com.breez.money_mind.controller;

import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.UsersDTO;
import com.breez.money_mind.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController {

	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public String getProfile(Model model) {
		Users currentUser = userService.getCurrentUser();

		model.addAttribute("name", currentUser.getName());
		model.addAttribute("username", currentUser.getUsername());

		return "profile";
	}

	@PostMapping("/update-user")
	public ResponseEntity<?> updateUser(@Valid @ModelAttribute("users") UsersDTO usersDTO,
										BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
					errors.put(error.getField(), error.getDefaultMessage())
			);
			return ResponseEntity.badRequest().body(errors);
		}

		Map<String, String> response = userService.updateCurrentUser(usersDTO);
		if (response.containsKey("error")) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

}
