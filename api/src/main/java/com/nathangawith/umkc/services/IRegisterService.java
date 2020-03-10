package com.nathangawith.umkc.services;

import java.util.Collection;

import com.nathangawith.umkc.dtos.Transaction;

public interface IRegisterService {
	public Collection<Transaction> getTransactions(int userID) throws Exception;
	public String getTotal(int userID) throws Exception;
}
