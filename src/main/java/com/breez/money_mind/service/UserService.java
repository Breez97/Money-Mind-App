package com.breez.money_mind.service;

import com.breez.money_mind.model.Notification;
import com.breez.money_mind.model.UserPrincipal;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.UsersDTO;
import com.breez.money_mind.repository.NotificationRepository;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private NotificationRepository notificationRepository;

	private List<UsersDTO> findAllUsers() {
		List<Users> allUsers = userRepository.findAll();
		return allUsers.stream()
				.map(users -> mapToUsersDTO(users))
				.collect(Collectors.toList());
	}

	public Users getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
			return userRepository.findByUsername(userPrincipal.getUsername());
		}
		throw new RuntimeException("User is not authenticated");
	}

	@Transactional
	public String saveUser(UsersDTO userDTO) {
		if (userRepository.findByUsername(userDTO.getUsername()) != null) {
			return "User with username " + userDTO.getUsername() + " already exists";
		}
		Users user = mapToUsers(userDTO);
		try {
			userRepository.save(user);
			Notification notification = Notification.builder()
					.telegramEnabled(false)
					.chatId((long) 0)
					.user(user)
					.build();
			notificationRepository.save(notification);
			return "User saved successfully";
		} catch (Exception e) {
			return "Error saving user " + e.getMessage();
		}
	}

	@Transactional
	public Map<String, String> updateCurrentUser(UsersDTO usersDTO) {
		Map<String, String> response = new HashMap<>();
		Users currentUser = getCurrentUser();

		if (usersDTO.getPassword() == null || !checkCurrentPassword(usersDTO.getPassword())) {
			response.put("error", "Incorrect current password");
			return response;
		}

		usersDTO.setId(currentUser.getId());
		List<UsersDTO> allUsers = findAllUsers();
		for (UsersDTO checkUser : allUsers) {
			if (Objects.equals(checkUser.getUsername(), usersDTO.getUsername())
					&& !Objects.equals(checkUser.getId(), usersDTO.getId())) {
				response.put("error", "User with such username already exists");
				return response;
			}
		}

		Users users = mapToUsers(usersDTO);
		users.setId(currentUser.getId());
		if (!usersDTO.getNewPassword().isEmpty()) {
			users.setPassword(encoder.encode(usersDTO.getNewPassword()));
		}

		try {
			userRepository.save(users);
			authenticateUser(users);
			response.put("message", "User updated successfully");
		} catch (Exception e) {
			response.put("error", "Error updating user: " + e.getMessage());
		}

		return response;
	}

	private void authenticateUser(Users updatedUser) {
		UserPrincipal userPrincipal = new UserPrincipal(updatedUser);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				userPrincipal,
				null,
				userPrincipal.getAuthorities()
		);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	public boolean checkCurrentPassword(String rawPassword) {
		Users currentUser = getCurrentUser();
		return encoder.matches(rawPassword, currentUser.getPassword());
	}

	private Users mapToUsers(UsersDTO userDTO) {
		return Users.builder()
				.id(userDTO.getId())
				.name(userDTO.getName())
				.username(userDTO.getUsername())
				.password(encoder.encode(userDTO.getPassword()))
				.build();
	}

	private UsersDTO mapToUsersDTO(Users users) {
		return UsersDTO.builder()
				.id(users.getId())
				.name(users.getName())
				.username(users.getUsername())
				.password(users.getPassword())
				.build();
	}

}
