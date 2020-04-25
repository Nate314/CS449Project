package com.nathangawith.umkc.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.repositories.ISettingsRepository;

@Component("settings_service")
public class SettingsService implements ISettingsService {

    private ISettingsRepository mSettingsRepository;

    @Autowired
    public SettingsService(
        @Qualifier("settings_repository")
        ISettingsRepository aSettingsRepository
    ) {
        this.mSettingsRepository = aSettingsRepository;
    }

	@Override
	public boolean addAccount(int userID, String description, boolean showWarning) throws Exception {
		DBAccount account = mSettingsRepository.selectAccount(userID, description);
		if (account != null) {
			if (account.Enabled == 1) throw new Exception(Messages.ACCOUNT_ALREADY_EXISTS);
			else if (showWarning) throw new Exception(Messages.ACCOUNT_DISABLED);
			else return mSettingsRepository.updateAccountEnable(userID, account.AccountID);
		}
		else return mSettingsRepository.insertAccount(userID, description);
	}

	@Override
	public Collection<DBAccount> getAccounts(int userID) throws Exception {
		return mSettingsRepository.selectAccounts(userID);
	}
	
	@Override
	public boolean removeAccount(int userID, int id, boolean showWarning) throws Exception {
		boolean isAccountUsed = mSettingsRepository.isAccountUsedInTransactions(userID, id);
		if (isAccountUsed && showWarning) throw new Exception(Messages.ACCOUNT_USED_IN_TRANSACTIONS);
		if (isAccountUsed) {
			return mSettingsRepository.updateAccountDisable(userID, id);
		} else {
			return mSettingsRepository.deleteAccount(userID, id);
		}
	}

	@Override
	public boolean addCategory(int userID, String categoryType, String description, boolean showWarning) throws Exception {
		DBCategory category = mSettingsRepository.selectCategory(userID, categoryType, description);
		if (category != null) {
			if (category.Enabled == 1) throw new Exception(Messages.CATEGORY_ALREADY_EXISTS);
			else if (showWarning) throw new Exception(Messages.CATEGORY_DISABLED);
			else return mSettingsRepository.updateCategoryEnable(userID, category.CategoryID);
		}
		else return mSettingsRepository.insertCategory(userID, categoryType, description);
	}

	@Override
	public Collection<DBCategory> getCategories(int userID, String categoryType) throws Exception {
		return mSettingsRepository.selectCategories(userID, categoryType);
	}

	@Override
	public boolean removeCategory(int userID, int id, boolean showWarning) throws Exception {
		boolean isCategoryUsed = mSettingsRepository.isCategoryUsedInTransactions(userID, id);
		if (isCategoryUsed && showWarning) throw new Exception(Messages.CATEGORY_USED_IN_TRANSACTIONS);
		if (isCategoryUsed) {
			return mSettingsRepository.updateCategoryDisable(userID, id);
		} else {
			return mSettingsRepository.deleteCategory(userID, id);
		}
	}
}
