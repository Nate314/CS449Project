package com.nathangawith.umkc.services;

import java.util.Date;

public interface ITransactionsService {
	public boolean addNewTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date);
}
