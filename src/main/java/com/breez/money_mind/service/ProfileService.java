package com.breez.money_mind.service;

import com.breez.money_mind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

	@Autowired
	private UserRepository userRepository;

}
