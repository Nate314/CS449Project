package com.nathangawith.umkc.financeapp.components;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;

public class RegisterEntry {

    private int imgResID;
    private String description;
    private String amount;
    private String date;
    private String category;
    private String account;

    private String stringifyTransactionAmount(double amount) {
        boolean negative = amount < 0;
        amount = Math.abs(amount);
        int intCents = ((int) ((amount * 100) % 100));
        String dollars = ((int) amount) + "";
        String cents = String.format("%s%s", intCents < 10 ? "0" : "", intCents + "");
        return String.format("%s$%s.%s", negative ? "-" : "", dollars, cents);
    }

    public RegisterEntry(TransactionDto transaction) {
        this.imgResID = transaction.IsIncome ? R.drawable.income_green : R.drawable.expense_red;
        this.description = transaction.Description;
        this.amount = this.stringifyTransactionAmount(transaction.Amount);
        this.date = transaction.Date;
        this.account = transaction.AccountDescription;
        this.category = transaction.CategoryDescription;
    }

    public int getImgResID() { return this.imgResID; }
    public String getDescription() { return this.description; }
    public String getAmount() { return this.amount; }
    public String getDate() { return this.date; }
    public String getAccount() { return this.account; }
    public String getCategory() { return this.category; }
}
