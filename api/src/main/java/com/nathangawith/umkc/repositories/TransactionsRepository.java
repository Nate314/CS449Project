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
import com.nathangawith.umkc.dtos.DBTransfer;

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
		List<String> params = Algorithms.params(userID);
		Collection<DBTransaction> queryResult = DB.select(Queries.GET_FROM_TO_TRANSACTION_IDS, params, DBTransaction.class);
		List<Integer> transactionIDs = queryResult.stream().map(transaction -> transaction.TransactionID).collect(Collectors.toList());
		if (transactionIDs.size() != 2) {
			return false;
		} else {
			int toTransactionID = transactionIDs.get(0);
			int fromTransactionID = transactionIDs.get(1);
			params = Algorithms.params(userID, fromTransactionID, toTransactionID);
			return DB.execute(Queries.INSERT_TRANSFER, params);
		}
	}
	
	@Override
	public DBTransfer selectTransfer(int transactionID) {
		String sql = "SELECT * FROM transfers WHERE TransactionFromID = ? OR TransactionToID = ?";
		List<String> params = Algorithms.params(transactionID, transactionID);
		return DB.selectFirst(sql, params, DBTransfer.class);
	}

	@Override
	public boolean updateTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date, int transactionID) {
		String accID = accountID == -1 ? (String) null : accountID + "";
		String catID = categoryID == -1 ? (String) null : categoryID + "";
		String sql = "UPDATE transactions SET AccountID = ?, CategoryID = ?, Description = ?, Amount = ?, Date = ? WHERE UserID = ? AND TransactionID = ?";
		List<String> params = Algorithms.params(accID, catID, description, amount, Algorithms.dateToString(date), userID, transactionID);
		return DB.execute(sql, params);
	}

	@Override
	public boolean deleteTransaction(int userID, int transactionID) {
		String sql = "DELETE FROM transactions WHERE UserID = ? AND TransactionID = ?";
		List<String> params = Algorithms.params(userID, transactionID);
		return DB.execute(sql, params);
	}

	@Override
	public boolean deleteTransfer(int userID, int transactionID) {
		boolean success = true;
		String sql;
		List<String> params;
		DBTransfer transfer = this.selectTransfer(transactionID);
		if (transfer == null) return false;
		sql = "DELETE FROM transfers WHERE UserID = ? AND TransferID = ?";
		params = Algorithms.params(userID, transfer.TransferID);
		success = success && DB.execute(sql, params);
		sql = "DELETE FROM transactions WHERE UserID = ? AND TransactionID IN (?, ?)";
		params = Algorithms.params(userID, transfer.TransactionFromID, transfer.TransactionToID);
		success = success && DB.execute(sql, params);
		return success;
	}
}
