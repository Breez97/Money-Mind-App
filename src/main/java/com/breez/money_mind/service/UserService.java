package com.breez.money_mind.service;

import com.breez.money_mind.model.Users;
import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public Users registerUser(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

}
