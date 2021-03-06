package com.nathangawith.umkc.repositories;

import java.util.Date;

import com.nathangawith.umkc.dtos.DBTransfer;

public interface ITransactionsRepository {
	public boolean    insertTransaction (int userID, int accountID, int categoryID,                            String description, double amount, Date date);
	public boolean    insertTransfer    (int userID, int fromID,    int toID,       boolean isAccountTransfer, String description, double amount, Date date);
	public boolean    updateTransaction (int userID, int accountID, int categoryID,                            String description, double amount, Date date, int transactionID);
	public boolean    deleteTransaction (int userID,                                                                                                         int transactionID);
	public boolean    deleteTransfer    (int userID,                                                                                                         int transactionID);
	public DBTransfer selectTransfer    (                                                                                                                    int transactionID);
}
