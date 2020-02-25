package com.nathangawith.umkc.repositories;

public interface ISettingsRepository {
	boolean doesAccountExist(int userID, String description);
	boolean insertAccount(int userID, String description);
}
