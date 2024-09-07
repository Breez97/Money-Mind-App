package com.breez.money_mind.repository;

import com.breez.money_mind.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	List<Subscription> findByUserId(Integer userId);

}
