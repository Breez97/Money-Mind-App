package com.breez.money_mind.repository;

import com.breez.money_mind.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	Notification findByUserId(Integer userId);

}
