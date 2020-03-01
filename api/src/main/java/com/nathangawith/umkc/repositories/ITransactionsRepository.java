package com.nathangawith.umkc.repositories;

import java.util.Date;

public interface ITransactionsRepository {
	public boolean insertTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date);
}
