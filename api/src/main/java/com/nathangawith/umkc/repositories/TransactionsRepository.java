package com.nathangawith.umkc.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;

@Component("transactions_repository")
public class TransactionsRepository implements ITransactionsRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public boolean insertTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date) {
		List<String> params = Algorithms.params(userID, accountID, categoryID, description, amount, Algorithms.dateToString(date));
		boolean success = DB.execute(Queries.INSERT_TRANSACTION, params);
		return success;
	}
}
