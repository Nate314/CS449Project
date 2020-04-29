package com.nathangawith.umkc.services;

import java.util.Date;

public interface ITransactionsService {
	public boolean addNewTransaction (int userID, int accountID, int categoryID,                            String description, double amount, Date date);
	public boolean addNewTransfer    (int userID, int fromID,    int toID,       boolean isAccountTransfer, String description, double amount, Date date);
	public boolean editTransaction   (int userID, int accountID, int categoryID,                            String description, double amount, Date date, int transactionID);
	public boolean editTransfer      (int userID, int fromID,    int toID,       boolean isAccountTransfer, String description, double amount, Date date, int transactionID);
	public boolean deleteTransaction (int userID,                                                                                                         int transactionID);
	public boolean deleteTransfer    (int userID,                                                                                                         int transactionID);
}
