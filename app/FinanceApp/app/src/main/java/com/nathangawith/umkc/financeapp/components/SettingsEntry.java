package com.nathangawith.umkc.financeapp.components;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;

public class SettingsEntry {

    private int id;
    private String description;
    private boolean isAccount;
    private boolean isIncomeCategory;
    private double total;

    public SettingsEntry(DBAccount account) {
        this.description = account.Description + "                                                                                                                       .";
        this.id = account.AccountID;
        this.total = account.Total;
        this.isAccount = true;
    }

    public SettingsEntry(DBCategory category, boolean isIncomeCategory) {
        this.description = category.Description + "                                                                                                                       .";
        this.id = category.CategoryID;
        this.total = category.Total;
        this.isAccount = false;
        this.isIncomeCategory = isIncomeCategory;
    }

    public int getID() { return this.id; }
    public String getDescription() { return this.description; }
    public boolean getIsAccount() { return this.isAccount; }
    public boolean getIsIncomeCategory() { return this.isIncomeCategory; }
    public double getTotal() { return this.total; }
}
