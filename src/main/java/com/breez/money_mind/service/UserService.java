package com.breez.money_mind.service;

import com.breez.money_mind.exceptions.UserAlreadyExistsException;
import com.breez.money_mind.model.Users;
import com.breez.money_mind.model.dto.UsersDTO;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JWTService jwtService;

	public Users saveUser(UsersDTO userDTO) {
		if (userRepository.findByUsername(userDTO.getUsername()) != null) {
			throw new UserAlreadyExistsException("User with username " + userDTO.getUsername() + " already exists.");
		}
		Users user = mapToUsers(userDTO);
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	private Users mapToUsers(UsersDTO userDTO) {
		return Users.builder()
				.id(userDTO.getId())
				.name(userDTO.getName())
				.username(userDTO.getUsername())
				.password(userDTO.getPassword())
				.build();
	}

}
