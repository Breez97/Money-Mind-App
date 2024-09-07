package com.breez.money_mind.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String telegram;
	private Boolean telegramEnabled;
	private String email;
	private Boolean emailEnabled;

	@OneToOne
	@JoinTable(
			name = "user_notifications",
			joinColumns = @JoinColumn(name = "notification_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Users user;

}
