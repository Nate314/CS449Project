package com.nathangawith.umkc;

public class Queries {
	public static final String GET_USER           = "SELECT * FROM users WHERE username = ?";
	public static final String GET_USER_PASSWORD  = "SELECT * FROM users WHERE username = ? AND password = ?";
	public static final String INSERT_USER        = "INSERT INTO users (username, password) VALUES (?, ?)";
	public static final String GET_ACCOUNT        = "SELECT * FROM accounts WHERE userid = ? AND description = ?";
	public static final String GET_ACCOUNTS       = "SELECT * FROM accounts WHERE userid = ?";
	public static final String INSERT_ACCOUNT     = "INSERT INTO accounts (userid, description) VALUES (?, ?)";
	public static final String GET_CATEGORY       = "SELECT * FROM categories WHERE userid = ? AND type = ? AND description = ?";
	public static final String GET_CATEGORIES     = "SELECT * FROM categories WHERE userid = ? AND type = ?";
	public static final String INSERT_CATEGORY    = "INSERT INTO categories (userid, type, description) VALUES (?, ?, ?)";
	public static final String INSERT_TRANSACTION = "INSERT INTO transactions (UserID, AccountID, CategoryID, Description, Amount, Date) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String GET_REGISTER_TOTAL = "SELECT SUM(amount) AS DOUBLE_RESPONSE FROM transactions WHERE userid = ?";
	public static final String GET_TRANSACTIONS   = 
		"SELECT transactions.transactionid, transactions.userid, transactions.accountid,"
		+ "\n\t" + "transactions.description, transactions.amount, transactions.date,"
		+ "\n\t" + "accounts.description AS accountdescription, categories.description AS categorydescription" 
		+ "\n" + "FROM transactions" 
		+ "\n\t" + "JOIN accounts ON transactions.AccountID = accounts.AccountID" 
		+ "\n\t" + "JOIN categories ON transactions.CategoryID = categories.CategoryID" 
		+ "\n" + "WHERE transactions.userid = ?";
}
