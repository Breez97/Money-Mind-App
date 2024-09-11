package com.breez.money_mind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyMindApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyMindApplication.class, args);
	}

}
