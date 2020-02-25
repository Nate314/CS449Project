package com.nathangawith.umkc.services;

public interface ISettingsService {
	boolean addAccount(int userID, String description) throws Exception;
	boolean addCategory(int userID, String categoryType, String description) throws Exception;
}
