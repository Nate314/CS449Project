package com.nathangawith.umkc.services;

import java.util.Collection;

import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;

public interface ISettingsService {
	public Collection<DBAccount>  getAccounts    (int userID)                                                                throws Exception;
	public boolean                addAccount     (int userID, String description,  boolean showWarning)                      throws Exception;
	public boolean                removeAccount  (int userID, int id,              boolean showWarning)                      throws Exception;
	public Collection<DBCategory> getCategories  (int userID, String categoryType)                                           throws Exception;
	public boolean                addCategory    (int userID, String categoryType, String description, boolean showWarning)  throws Exception;
	public boolean                removeCategory (int userID, int id,              boolean showWarning)                      throws Exception;
}
