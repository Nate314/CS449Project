package com.nathangawith.umkc.dtos;

import java.util.Date;

public class Transaction {
	public boolean IsIncome;
	public int     TransactionID;
	public int     UserID;
	public int     AccountID;
	public String  AccountDescription;
	public String  CategoryDescription;
	public String  Description;
	public double  Amount;
	public Date    Date;
}