package com.nathangawith.umkc.repositories;

import java.util.Collection;

import com.nathangawith.umkc.dtos.Transaction;

public interface IRegisterRepository {
	public Collection<Transaction> selectTransactions(int userID);
	public double selectTotal(int userID);
}
