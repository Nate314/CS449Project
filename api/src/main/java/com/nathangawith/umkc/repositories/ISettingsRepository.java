package com.nathangawith.umkc.repositories;

public interface ISettingsRepository {
	boolean doesAccountExist(int userID, String description);
	boolean insertAccount(int userID, String description);
	boolean doesCategoryExist(int userID, String categoryType, String description);
	boolean insertCategory(int userID, String categoryType, String description);
}
