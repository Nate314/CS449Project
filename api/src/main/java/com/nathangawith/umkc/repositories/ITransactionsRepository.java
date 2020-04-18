package com.nathangawith.umkc.repositories;

import java.util.Date;

public interface ITransactionsRepository {
	public boolean insertTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date);
	public boolean insertTransfer(int userID, int fromID, int toID, boolean isAccountTransfer, String description, double amount, Date date);
}
