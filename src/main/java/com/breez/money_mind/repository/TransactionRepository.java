package com.breez.money_mind.repository;

import com.breez.money_mind.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	List<Transaction> findByUsersId(Integer userId);

	/*
	* Using Queries
	*
	* @Query("SELECT t FROM Transaction t JOIN t.users u WHERE u.id = :userId");
	* List<Transaction> findTransactionsByUserId(@Param("userId") Integer userId);
	*
	* @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND id in (:transactions)")
	* double sumByType(@Param("type") String type, @Param("ids") ArrayList<Transaction> transactions);
	*
	* */

}
