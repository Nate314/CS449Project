package com.nathangawith.umkc.repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.dtos.DBResponse;

@Component("settings_repository")
public class SettingsRepository implements ISettingsRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public boolean insertAccount(int userID, String description) {
		List<String> params = Arrays.asList(new String[] { userID + "", description });
		boolean success = DB.execute(Queries.INSERT_ACCOUNT, params);
		return success;
	}
	
	@Override
	public DBAccount selectAccount(int userID, String description) {
		List<String> params = Algorithms.params(userID, description);
		DBAccount account = DB.selectFirst(Queries.GET_ACCOUNT, params, DBAccount.class);
		return account;
	}

	@Override
	public DBAccount selectAccount(int userID, int id) {
		return this.selectAccounts(userID).stream().filter(x -> x.AccountID == id).findFirst().orElse(null);
	}
	
	@Override
	public Collection<DBAccount> selectAccounts(int userID) {
		List<String> params = Arrays.asList(new String[] { userID + "" });
		Collection<DBAccount> accounts = DB.select(Queries.GET_ACCOUNTS, params, DBAccount.class);
		return accounts;
	}
	
	@Override
	public boolean deleteAccount(int userID, int id) {
		return DB.execute(Queries.DELETE_ACCOUNT, Algorithms.params(userID, id));
	}
	
	private boolean updateAccountEnableDisable(int userID, int id, boolean enable) {
		return DB.execute(Queries.UPDATE_ACCOUNT_ENABLE_DISABLE, Algorithms.params(enable ? 1 : 0, userID, id));
	}
	
	@Override
	public boolean updateAccountDisable(int userID, int id) {
		return this.updateAccountEnableDisable(userID, id, false);
	}
	
	@Override
	public boolean updateAccountEnable(int userID, int id) {
		return this.updateAccountEnableDisable(userID, id, true);
	}

	@Override
	public boolean isAccountUsedInTransactions(int userID, int id) {
		List<String> params = Algorithms.params(userID, id);
		DBResponse queryResult = DB.selectFirst(Queries.GET_ACCOUNT_IN_TRANSACTIONS_COUNT, params, DBResponse.class);
		return queryResult != null ? queryResult.INT_RESPONSE > 0 : true;
	}

	@Override
	public boolean updateAccount(int userID, int id, String description) {
		return DB.execute(Queries.UPDATE_ACCOUNT_DESCRIPTION, Algorithms.params(description, userID, id));
	}
	
	@Override
	public boolean insertCategory(int userID, String categoryType, String description) {
		List<String> params = Arrays.asList(new String[] { userID + "", categoryType, description });
		boolean success = DB.execute(Queries.INSERT_CATEGORY, params);
		return success;
	}
	
	@Override
	public DBCategory selectCategory(int userID, String categoryType, String description) {
		List<String> params = Algorithms.params(userID, categoryType, description);
		DBCategory category = DB.selectFirst(Queries.GET_CATEGORY, params, DBCategory.class);
		return category;
	}

	@Override
	public DBCategory selectCategory(int userID, String type, int id) {
		return this.selectCategories(userID, type).stream().filter(x -> x.CategoryID == id).findFirst().orElse(null);
	}
	
	@Override
	public Collection<DBCategory> selectCategories(int userID, String categoryType) {
		List<String> params = Arrays.asList(new String[] { userID + "", categoryType });
		Collection<DBCategory> categories = DB.select(Queries.GET_CATEGORIES, params, DBCategory.class);
		return categories;
	}
	
	@Override
	public boolean deleteCategory(int userID, int id) {
		return DB.execute(Queries.DELETE_CATEGORY, Algorithms.params(userID, id));
	}
	
	private boolean updateCategoryEnableDisable(int userID, int id, boolean enable) {
		return DB.execute(Queries.UPDATE_CATEGORY_ENABLE_DISABLE, Algorithms.params(enable ? 1 : 0, userID, id));
	}
	
	@Override
	public boolean updateCategoryDisable(int userID, int id) {
		return this.updateCategoryEnableDisable(userID, id, false);
	}
	
	@Override
	public boolean updateCategoryEnable(int userID, int id) {
		return this.updateCategoryEnableDisable(userID, id, true);
	}

	@Override
	public boolean isCategoryUsedInTransactions(int userID, int id) {
		List<String> params = Algorithms.params(userID, id);
		DBResponse queryResult = DB.selectFirst(Queries.GET_CATEGORY_IN_TRANSACTIONS_COUNT, params, DBResponse.class);
		return queryResult != null ? queryResult.INT_RESPONSE > 0 : true;
	}

	@Override
	public boolean updateCategory(int userID, int id, String description) {
		return DB.execute(Queries.UPDATE_CATEGORY_DESCRIPTION, Algorithms.params(description, userID, id));
	}
}
