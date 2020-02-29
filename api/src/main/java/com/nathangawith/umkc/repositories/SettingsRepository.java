package com.nathangawith.umkc.repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;

@Component("settings_repository")
public class SettingsRepository implements ISettingsRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public boolean doesAccountExist(int userID, String description) {
		List<String> params = Arrays.asList(new String[] { userID + "", description });
		DBAccount account = DB.selectFirst(Queries.GET_ACCOUNT, params, DBAccount.class);
		return account != null;
	}

	@Override
	public boolean insertAccount(int userID, String description) {
		List<String> params = Arrays.asList(new String[] { userID + "", description });
		boolean success = DB.execute(Queries.INSERT_ACCOUNT, params);
		return success;
	}
	
	@Override
	public Collection<DBAccount> selectAccounts(int userID) {
		List<String> params = Arrays.asList(new String[] { userID + "" });
		Collection<DBAccount> accounts = DB.select(Queries.GET_ACCOUNTS, params, DBAccount.class);
		return accounts;
	}
	
	@Override
	public boolean doesCategoryExist(int userID, String categoryType, String description) {
		List<String> params = Arrays.asList(new String[] { userID + "", categoryType, description });
		DBCategory category = DB.selectFirst(Queries.GET_CATEGORY, params, DBCategory.class);
		return category != null;
	}
	
	@Override
	public boolean insertCategory(int userID, String categoryType, String description) {
		List<String> params = Arrays.asList(new String[] { userID + "", categoryType, description });
		boolean success = DB.execute(Queries.INSERT_CATEGORY, params);
		return success;
	}
	
	@Override
	public Collection<DBCategory> selectCategories(int userID, String categoryType) {
		List<String> params = Arrays.asList(new String[] { userID + "", categoryType });
		Collection<DBCategory> categories = DB.select(Queries.GET_CATEGORIES, params, DBCategory.class);
		return categories;
	}
}
