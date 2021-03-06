package com.nathangawith.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Constants;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.dtos.DBCategory;
import com.nathangawith.umkc.repositories.SettingsRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositorySettingsTest extends BaseTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private SettingsRepository mRepository = new SettingsRepository();

	@Test
	public void doesAccountExistTest() {
		doesAccountExistGenericTest(true);
		doesAccountExistGenericTest(false);
	}
	
	@Test
	public void insertAccountTest() {
		insertAccountGenericTest(true);
		insertAccountGenericTest(false);
	}
	
	@Test
	public void selectAccountsTest() {
		selectAccountsGenericTest();
	}

	@Test
	public void doesCategoryExistTest() {
		for (String categoryType : new String[] { Constants.INCOME, Constants.EXPENSE }) {
			doesCategoryExistGenericTest(categoryType, true);
			doesCategoryExistGenericTest(categoryType, false);
		}
	}
	
	@Test
	public void insertCategoryTest() {
		for (String categoryType : new String[] { Constants.INCOME, Constants.EXPENSE }) {
			insertCategoryGenericTest(categoryType, true);
			insertCategoryGenericTest(categoryType, false);
		}
	}
	
	@Test
	public void selectCategoriesTest() {
		for (String categoryType : new String[] { Constants.INCOME, Constants.EXPENSE }) {
			selectCategoriesGenericTest(categoryType);
			selectCategoriesGenericTest(categoryType);
		}
	}
	
	private void doesAccountExistGenericTest(boolean expectedResult) {
		// Arrange
		String sql = Queries.GET_ACCOUNT;
		DBAccount account = new DBAccount();
		account.UserID = 1;
		account.Description = "some_account";
		List<String> params = Arrays.asList(new String[] { account.UserID + "", account.Description });
		Mockito.when(mDatabase.selectFirst(sql, params, DBAccount.class)).thenReturn(expectedResult ? account : null);

		// Act
		boolean valid = mRepository.doesAccountExist(1, "some_account");
		
		// Assert
		Assert.assertEquals(expectedResult, valid);
	}

	private void insertAccountGenericTest(boolean expectedResult) {
		// Arrange
		String sql = Queries.INSERT_ACCOUNT;
		List<String> params = Arrays.asList(new String[] { "1", "some_account" });
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(expectedResult);
		
		// Act
		boolean success = mRepository.insertAccount(1, "some_account");
		
		// Assert
		Assert.assertEquals(expectedResult, success);
	}
	
	private void selectAccountsGenericTest() {
		// Arrange
		String sql = Queries.GET_ACCOUNTS;
		ArrayList<DBAccount> accounts = new ArrayList<DBAccount>();
		DBAccount account = new DBAccount();
		account.UserID = 1;
		account.AccountID = 1;
		account.Description = "Account";
		accounts.add(account);
		List<String> params = Arrays.asList(new String[] { account.UserID + "" });
		Mockito.when(mDatabase.select(sql, params, DBAccount.class)).thenReturn(accounts);

		// Act
		Collection<DBAccount> result = mRepository.selectAccounts(1);
		
		// Assert
		Assert.assertEquals(result, accounts);
	}
	
	private void doesCategoryExistGenericTest(String categoryType, boolean expectedResult) {
		// Arrange
		String sql = Queries.GET_CATEGORY;
		DBCategory category = new DBCategory();
		category.UserID = 1;
		category.Description = "some_category";
		List<String> params = Arrays.asList(new String[] { category.UserID + "", categoryType, category.Description });
		Mockito.when(mDatabase.selectFirst(sql, params, DBCategory.class)).thenReturn(expectedResult ? category : null);

		// Act
		boolean valid = mRepository.doesCategoryExist(1, categoryType, "some_category");
		
		// Assert
		Assert.assertEquals(expectedResult, valid);
	}

	private void insertCategoryGenericTest(String categoryType, boolean expectedResult) {
		// Arrange
		String sql = Queries.INSERT_CATEGORY;
		List<String> params = Arrays.asList(new String[] { "1", categoryType, "some_category" });
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(expectedResult);
		
		// Act
		boolean success = mRepository.insertCategory(1, categoryType, "some_category");
		
		// Assert
		Assert.assertEquals(expectedResult, success);
	}

	private void selectCategoriesGenericTest(String categoryType) {
		// Arrange
		String sql = Queries.GET_CATEGORIES;
		ArrayList<DBCategory> categories = new ArrayList<DBCategory>();
		DBCategory category = new DBCategory();
		category.UserID = 1;
		category.CategoryID = 1;
		category.Description = "some_category";
		categories.add(category);
		List<String> params = Arrays.asList(new String[] { category.UserID + "", categoryType });
		Mockito.when(mDatabase.select(sql, params, DBCategory.class)).thenReturn(categories);

		// Act
		Collection<DBCategory> result = mRepository.selectCategories(1, categoryType);
		
		// Assert
		Assert.assertEquals(result, categories);
	}
}
