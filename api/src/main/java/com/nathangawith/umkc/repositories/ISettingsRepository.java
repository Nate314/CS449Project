package com.nathangawith.umkc.repositories;

import java.util.Collection;

import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;

public interface ISettingsRepository {
	public DBAccount              selectAccount                (int userID, String description);
	public DBAccount              selectAccount                (int userID, int id);
	public Collection<DBAccount>  selectAccounts               (int userID);
	public boolean                insertAccount                (int userID, String description);
	public boolean                deleteAccount                (int userID, int id);
	public boolean                updateAccountDisable         (int userID, int id);
	public boolean                updateAccountEnable          (int userID, int id);
	public boolean                isAccountUsedInTransactions  (int userID, int id);
	public boolean                updateAccount                (int userID, int id,              String description);
	public DBCategory             selectCategory               (int userID, String categoryType, String description);
	public DBCategory             selectCategory               (int userID, String categoryType, int id);
	public Collection<DBCategory> selectCategories             (int userID, String categoryType);
	public boolean                insertCategory               (int userID, String categoryType, String description);
	public boolean                deleteCategory               (int userID, int id);
	public boolean                updateCategoryDisable        (int userID, int id);
	public boolean                updateCategoryEnable         (int userID, int id);
	public boolean                isCategoryUsedInTransactions (int userID, int id);
	public boolean                updateCategory               (int userID, int id,              String description);
}
