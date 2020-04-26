package com.nathangawith.umkc;

public class Queries {
	public static final String GET_USER           = "SELECT * FROM users WHERE username = ?";
	public static final String GET_USER_PASSWORD  = "SELECT * FROM users WHERE username = ? AND password = ?";
	public static final String INSERT_USER        = "INSERT INTO users (username, password) VALUES (?, ?)";
	public static final String GET_ACCOUNT        =
			"SELECT accounts.*, CASE WHEN SUM(transactions.Amount) IS NULL THEN 0 ELSE SUM(transactions.Amount) END AS Total" 
		+ "\n\t" + "FROM accounts LEFT OUTER JOIN transactions ON transactions.AccountID = accounts.AccountID" 
		+ "\n\t" + "WHERE accounts.UserID = ? AND accounts.Description = ? GROUP BY accounts.AccountID";
	public static final String GET_ACCOUNTS       =
			"SELECT accounts.*, CASE WHEN SUM(transactions.Amount) IS NULL THEN 0 ELSE SUM(transactions.Amount) END AS Total" 
		+ "\n\t" + "FROM accounts LEFT OUTER JOIN transactions ON transactions.AccountID = accounts.AccountID" 
		+ "\n\t" + "WHERE accounts.UserID = ? AND accounts.Enabled = 1 GROUP BY accounts.AccountID";
	public static final String INSERT_ACCOUNT     = "INSERT INTO accounts (userid, description, enabled) VALUES (?, ?, 1)";
	public static final String GET_CATEGORY       =
			"SELECT categories.*, CASE WHEN SUM(transactions.Amount) IS NULL THEN 0 ELSE SUM(transactions.Amount) END AS Total"
		+ "\n\t" + "FROM categories LEFT OUTER JOIN transactions ON transactions.AccountID = categories.CategoryID"
		+ "\n\t" + "WHERE categories.UserID = ? AND categories.Type = ? AND categories.Description = ? GROUP BY categories.CategoryID";
	public static final String GET_CATEGORIES     =
			"SELECT categories.*, CASE WHEN SUM(transactions.Amount) IS NULL THEN 0 ELSE SUM(transactions.Amount) END AS Total"
		+ "\n\t" + "FROM categories LEFT OUTER JOIN transactions ON transactions.CategoryID = categories.CategoryID"
		+ "\n\t" + "WHERE categories.UserID = ? AND categories.Type = ? AND categories.Enabled = 1 GROUP BY categories.CategoryID";
	public static final String INSERT_CATEGORY    = "INSERT INTO categories (userid, type, description, enabled) VALUES (?, ?, ?, 1)";
	public static final String INSERT_TRANSACTION = "INSERT INTO transactions (UserID, AccountID, CategoryID, Description, Amount, Date) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String GET_REGISTER_TOTAL = "SELECT SUM(amount) AS DOUBLE_RESPONSE FROM transactions WHERE userid = ?";
	public static final String GET_TRANSACTIONS   =
				"SELECT transactions.transactionid, transactions.userid, transactions.accountid, transactions.categoryid,"
		+ "\n\t" + "transactions.description, transactions.amount, transactions.date,"
		+ "\n\t" + "accounts.description AS accountdescription, categories.description AS categorydescription,"
		+ "\n\t" + "NULL AS TransferID, NULL AS AccountFromDescription, NULL AS AccountToDescription,"
		+ "\n\t" + "NULL AS CategoryFromDescription, NULL AS CategoryToDescription,"
		+ "\n\t" + "NULL AS AccountFromID, NULL AS AccountToID,"
		+ "\n\t" + "NULL AS CategoryFromID, NULL AS CategoryToID"
		+ "\n" + "FROM transactions"
		+ "\n\t" + "JOIN accounts ON transactions.AccountID = accounts.AccountID"
		+ "\n\t" + "JOIN categories ON transactions.CategoryID = categories.CategoryID"
		+ "\n" + "WHERE transactions.userid = ?"
		+ "\n" + "UNION"
		+ "\n" + "SELECT NULL AS transactionid, transfers.UserID, NULL AS accountid, NULL AS categoryid, fromTransaction.Description, toTransaction.Amount, toTransaction.Date, NULL AS accountdescription, NULL AS categorydescription,"
		+ "\n" + "transfers.TransferID,"
		+ "\n" + "fromAccount.Description AS AccountFromDescription, toAccount.Description AS AccountToDescription,"
		+ "\n" + "fromCategory.Description AS CategoryFromDescription, toCategory.Description AS CategoryToDescription,"
		+ "\n" + "fromAccount.AccountID AS AccountFromID, toAccount.AccountID AS AccountToID,"
		+ "\n" + "fromCategory.CategoryID AS CategoryFromID, toCategory.CategoryID AS CategoryToID"
		+ "\n" + "FROM transfers"
		+ "\n\t" + "LEFT JOIN transactions fromTransaction ON fromTransaction.TransactionID = transfers.TransactionFromID"
		+ "\n\t" + "LEFT JOIN transactions toTransaction ON toTransaction.TransactionID = transfers.TransactionToID"
		+ "\n\t" + "LEFT JOIN accounts fromAccount ON fromTransaction.AccountID = fromAccount.AccountID"
		+ "\n\t" + "LEFT JOIN categories fromCategory ON fromTransaction.CategoryID = fromCategory.CategoryID"
		+ "\n\t" + "LEFT JOIN accounts toAccount ON toTransaction.AccountID = toAccount.AccountID"
		+ "\n\t" + "LEFT JOIN categories toCategory ON toTransaction.CategoryID = toCategory.CategoryID"
		+ "\n" + "WHERE transfers.userid = ?"
		+ "\n" + "ORDER BY Date DESC, transactionid DESC";
	public static final String GET_REPORT_SELECT_FROM_WHERE =
				 "SELECT SUM(transactions.Amount) AS Amount, transactions.Date,"
		+ "\n\t" + "accounts.Description AS AccountDescription, categories.Description AS CategoryDescription," 
	 	+ "\n\t" + "YEAR(Date) AS Year, CONCAT(YEAR(Date), '-', MONTH(Date)) AS Month"
		+ "\n" + "FROM transactions"
		+ "\n\t" + "LEFT OUTER JOIN accounts ON transactions.AccountID = accounts.AccountID" 
		+ "\n\t" + "LEFT OUTER JOIN categories ON transactions.CategoryID = categories.CategoryID" 
		+ "\n" + "WHERE transactions.UserID = ?"
		+ "\n\t" + "AND transactions.Date BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d')";
	public static final String GET_REPORT_GROUP_BY_YEAR_ACCOUNT   = "\nGROUP BY Year, AccountDescription";
	public static final String GET_REPORT_GROUP_BY_YEAR_CATEGORY  = "\nGROUP BY Year, CategoryDescription";
	public static final String GET_REPORT_GROUP_BY_MONTH_ACCOUNT  = "\nGROUP BY Month, AccountDescription";
	public static final String GET_REPORT_GROUP_BY_MONTH_CATEGORY = "\nGROUP BY Month, CategoryDescription";
	public static final String GET_FROM_TO_TRANSACTION_IDS        = "SELECT TransactionID FROM Transactions WHERE UserID = ? ORDER BY TransactionID DESC LIMIT 2";
	public static final String INSERT_TRANSFER                    = "INSERT INTO transfers (UserID, TransactionFromID, TransactionToID) VALUES (?, ?, ?)";
	public static final String GET_ACCOUNT_IN_TRANSACTIONS_COUNT  = "SELECT COUNT(*) AS INT_RESPONSE FROM transactions WHERE UserID = ? AND AccountID = ?";
	public static final String UPDATE_ACCOUNT_ENABLE_DISABLE      = "UPDATE accounts SET Enabled = ? WHERE UserID = ? AND AccountID = ?";
	public static final String UPDATE_ACCOUNT_DESCRIPTION         = "UPDATE accounts SET Description = ? WHERE UserID = ? AND AccountID = ?";
	public static final String DELETE_ACCOUNT                     = "DELETE FROM accounts WHERE UserID = ? AND AccountID = ?";
	public static final String GET_CATEGORY_IN_TRANSACTIONS_COUNT = "SELECT COUNT(*) AS INT_RESPONSE FROM transactions WHERE UserID = ? AND CategoryID = ?";
	public static final String UPDATE_CATEGORY_ENABLE_DISABLE     = "UPDATE categories SET Enabled = ? WHERE UserID = ? AND CategoryID = ?";
	public static final String UPDATE_CATEGORY_DESCRIPTION        = "UPDATE categories SET Description = ? WHERE UserID = ? AND CategoryID = ?";
	public static final String DELETE_CATEGORY                    = "DELETE FROM categories WHERE UserID = ? AND CategoryID = ?";
}
