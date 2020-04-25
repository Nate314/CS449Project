package com.nathangawith.umkc.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBTransaction;

@Component("transactions_repository")
public class TransactionsRepository implements ITransactionsRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public boolean insertTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date) {
		String accID = accountID == -1 ? (String) null : accountID + "";
		String catID = categoryID == -1 ? (String) null : categoryID + "";
		List<String> params = Algorithms.params(userID, accID, catID, description, amount, Algorithms.dateToString(date));
		boolean success = DB.execute(Queries.INSERT_TRANSACTION, params);
		return success;
	}

	@Override
	public boolean insertTransfer(int userID, int fromID, int toID, boolean isAccountTransfer, String description, double amount, Date date) {
		boolean success = true;
		if (isAccountTransfer) {
			success = success && this.insertTransaction(userID, fromID, -1, description, -1 * amount, date);
			success = success && this.insertTransaction(userID, toID, -1, description, amount, date);
		} else {
			success = success && this.insertTransaction(userID, -1, fromID, description, -1 * amount, date);
			success = success && this.insertTransaction(userID, -1, toID, description, amount, date);
		}
		List<String> params = Algorithms.params(userID);
		Collection<DBTransaction> queryResult = DB.select(Queries.GET_FROM_TO_TRANSACTION_IDS, params, DBTransaction.class);
		List<Integer> transactionIDs = queryResult.stream().map(transaction -> transaction.TransactionID).collect(Collectors.toList());
		if (transactionIDs.size() != 2) {
			return false;
		} else {
			int toTransactionID = transactionIDs.get(0);
			int fromTransactionID = transactionIDs.get(1);
			params = Algorithms.params(userID, fromTransactionID, toTransactionID);
			success = success && DB.execute(Queries.INSERT_TRANSFER, params);
			return success;
		}
	}
}
