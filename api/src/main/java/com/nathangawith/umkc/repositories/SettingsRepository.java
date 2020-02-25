package com.nathangawith.umkc.repositories;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBAccount;

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
}