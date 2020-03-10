package com.nathangawith.umkc.repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBResponse;
import com.nathangawith.umkc.dtos.Transaction;

@Component("register_repository")
public class RegisterRepository implements IRegisterRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public Collection<Transaction> selectTransactions(int userID) {
		List<String> params = Arrays.asList(new String[] { userID + "" });
		Collection<Transaction> transactions = DB.select(Queries.GET_TRANSACTIONS, params, Transaction.class);
		return transactions;
	}
	
	@Override
	public double selectTotal(int userID) {
		List<String> params = Arrays.asList(new String[] { userID + "" });
		DBResponse queryResult = DB.selectFirst(Queries.GET_REGISTER_TOTAL, params, DBResponse.class);
		double total = queryResult.DOUBLE_RESPONSE;
		return total;
	}
}
