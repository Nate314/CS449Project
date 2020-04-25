package com.nathangawith.umkc.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Constants;
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
		DBAccount account = mSettingsRepository.selectAccount(userID, id);
		boolean isZeroBalance = account == null ? true : account.Total == 0;
		if (isAccountUsed && showWarning) throw new Exception(Messages.ACCOUNT_USED_IN_TRANSACTIONS);
		if (isZeroBalance) {
			if (isAccountUsed) {
				return mSettingsRepository.updateAccountDisable(userID, id);
			} else {
				return mSettingsRepository.deleteAccount(userID, id);
			}
		} else throw new Exception(Messages.ACCOUNT_HAS_NONZERO_BALANCE);
	}

	@Override
	public boolean editAccount(int userID, int id, String description) throws Exception {
		DBAccount account = mSettingsRepository.selectAccount(id, description);
		if (account != null) {
			if (account.Enabled == 1) throw new Exception(Messages.ACCOUNT_ALREADY_EXISTS);
			else throw new Exception(Messages.ACCOUNT_ALREADY_DISABLED);
		}
		else return mSettingsRepository.updateAccount(userID, id, description);
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
	
	private DBCategory getCategoryWithUnknownType(int userID, Integer id, String description) {
		DBCategory incomeCategory = null;
		DBCategory expenseCategory = null;
		if (description == null) {
			incomeCategory = mSettingsRepository.selectCategory(userID, Constants.INCOME, id);
			expenseCategory = mSettingsRepository.selectCategory(userID, Constants.EXPENSE, id);
		} else if (id == null) {
			incomeCategory = mSettingsRepository.selectCategory(userID, Constants.INCOME, description);
			expenseCategory = mSettingsRepository.selectCategory(userID, Constants.EXPENSE, description);
		}
		return incomeCategory != null ? incomeCategory : expenseCategory;
	}

	@Override
	public boolean removeCategory(int userID, int id, boolean showWarning) throws Exception {
		boolean isCategoryUsed = mSettingsRepository.isCategoryUsedInTransactions(userID, id);
		DBCategory category = this.getCategoryWithUnknownType(userID, id, null);
		boolean isZeroBalance = category == null ? true : category.Total == 0;
		if (isCategoryUsed && showWarning) throw new Exception(Messages.CATEGORY_USED_IN_TRANSACTIONS);
		if (isZeroBalance) {
			if (isCategoryUsed) {
				return mSettingsRepository.updateCategoryDisable(userID, id);
			} else {
				return mSettingsRepository.deleteCategory(userID, id);
			}
		} else throw new Exception(Messages.CATEGORY_HAS_NONZERO_BALANCE);
	}

	@Override
	public boolean editCategory(int userID, int id, String description) throws Exception {
		DBCategory category = this.getCategoryWithUnknownType(userID, null, description);
		if (category != null) {
			if (category.Enabled == 1) throw new Exception(Messages.CATEGORY_ALREADY_EXISTS);
			else throw new Exception(Messages.CATEGORY_ALREADY_DISABLED);
		}
		else return mSettingsRepository.updateCategory(userID, id, description);
	}
}
