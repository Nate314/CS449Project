package com.nathangawith.tests;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Constants;
import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.repositories.ISettingsRepository;
import com.nathangawith.umkc.services.SettingsService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceSettingsTest extends BaseTest {

	@Mock
	private ISettingsRepository mRepository;
	
	@InjectMocks
	private SettingsService mService = new SettingsService(mRepository);

	@Test
	public void addAccountTest() {
		addAccountGenericTest(true, true, false);
		addAccountGenericTest(true, false, false);
		addAccountGenericTest(false, true, true);
		addAccountGenericTest(false, false, false);
	}
	
	@Test
	public void getAccountsTest() {
		getAccountsGenericTest();
	}

	@Test
	public void addCategoryTest() {
		for (String categoryType : new String[] { Constants.INCOME, Constants.EXPENSE }) {
			addCategoryGenericTest(categoryType, true, true, false);
			addCategoryGenericTest(categoryType, true, false, false);
			addCategoryGenericTest(categoryType, false, true, true);
			addCategoryGenericTest(categoryType, false, false, false);
		}
	}
	
	@Test
	public void getCategoriesTest() {
		for (String categoryType : new String[] { Constants.INCOME, Constants.EXPENSE }) {
			selectCategoriesGenericTest(categoryType);
		}
	}

	private void addAccountGenericTest(boolean userExists, boolean insertSuccessful, boolean expectedResult) {
		// Arrange
		Mockito.when(mRepository.doesAccountExist(1, "some_account")).thenReturn(userExists);
		Mockito.when(mRepository.insertAccount(1, "some_account")).thenReturn(insertSuccessful);		
		
		// Act
		try {
			boolean success = mService.addAccount(1, "some_account");
			
			// Assert
			Assert.assertEquals(expectedResult, success);
		} catch (Exception e) {
			if (userExists) {
				Assert.assertEquals(e.getMessage(), Messages.ACCOUNT_ALREADY_EXISTS);
			} else {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}

	private void getAccountsGenericTest() {
		// Arrange
		ArrayList<DBAccount> accounts = new ArrayList<DBAccount>();
		DBAccount account = new DBAccount();
		account.UserID = 1;
		account.AccountID = 1;
		account.Description = "Account";
		accounts.add(account);
		Mockito.when(mRepository.selectAccounts(1)).thenReturn(accounts);
		Collection<DBAccount> result;
		try {		
			
			// Act
			result = mService.getAccounts(1);

			// Assert
			Assert.assertEquals(result, accounts);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	private void addCategoryGenericTest(String categoryType, boolean categoryExists, boolean insertSuccessful, boolean expectedResult) {
		// Arrange
		Mockito.when(mRepository.doesCategoryExist(1, categoryType, "some_category")).thenReturn(categoryExists);
		Mockito.when(mRepository.insertCategory(1, categoryType, "some_category")).thenReturn(insertSuccessful);		
		
		// Act
		try {
			boolean success = mService.addCategory(1, categoryType, "some_category");
			
			// Assert
			Assert.assertEquals(expectedResult, success);
		} catch (Exception e) {
			if (categoryExists) {
				Assert.assertEquals(e.getMessage(), Messages.CATEGORY_ALREADY_EXISTS);
			} else {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}

	private void selectCategoriesGenericTest(String categoryType) {
		// Arrange
		ArrayList<DBCategory> categories = new ArrayList<DBCategory>();
		DBCategory category = new DBCategory();
		category.UserID = 1;
		category.CategoryID = 1;
		category.Description = "some_category";
		categories.add(category);
		Mockito.when(mRepository.selectCategories(1, categoryType)).thenReturn(categories);		
		
		// Act
		try {
			Collection<DBCategory> result = mService.getCategories(1, categoryType);
			
			// Assert
			Assert.assertEquals(result, categories);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
