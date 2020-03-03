package com.nathangawith.umkc.repositories;

import java.util.Collection;

import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;

public interface ISettingsRepository {
	public Collection<DBAccount>  selectAccounts   (int userID);
	public boolean                doesAccountExist (int userID, String description);
	public boolean                insertAccount    (int userID, String description);
	public Collection<DBCategory> selectCategories (int userID, String categoryType);
	public boolean                doesCategoryExist(int userID, String categoryType, String description);
	public boolean                insertCategory   (int userID, String categoryType, String description);
}
