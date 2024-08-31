package com.breez.money_mind.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String errorMessage;

		if (exception instanceof UsernameNotFoundException) {
			errorMessage = "User not found.";
		} else if (exception instanceof BadCredentialsException) {
			if (exception.getCause() instanceof UsernameNotFoundException) {
				errorMessage = "User not found.";
			} else {
				errorMessage = "Invalid password.";
			}
		} else {
			errorMessage = "Authentication failure.";
		}

		setDefaultFailureUrl("/login-page?error=true&message=" + errorMessage);
		super.onAuthenticationFailure(request, response, exception);
	}

}
