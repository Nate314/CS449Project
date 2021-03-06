package com.nathangawith.umkc.dtos;

import java.util.Date;

public class Transaction {
	public boolean IsIncome;
	public boolean IsTransfer;
	public Integer TransactionID;
	public Integer UserID;
	public Integer AccountID;
	public Integer CategoryID;
	public String  AccountDescription;
	public String  CategoryDescription;
	public String  Description;
	public double  Amount;
	public Date    Date;
	public Integer TransferID;
	public String  AccountFromDescription;
	public String  AccountToDescription;
	public String  CategoryFromDescription;
	public String  CategoryToDescription;
	public Integer AccountToID;
	public Integer AccountFromID;
	public Integer CategoryToID;
	public Integer CategoryFromID;
}
