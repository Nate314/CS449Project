package com.nathangawith.tests;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBAccount;
import com.nathangawith.umkc.repositories.SettingsRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositorySettingsTest {

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
	public void addAccountTest() {
		addAccountGenericTest(true);
		addAccountGenericTest(false);
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

	public void addAccountGenericTest(boolean expectedResult) {
		// Arrange
		String sql = Queries.INSERT_ACCOUNT;
		List<String> params = Arrays.asList(new String[] { "1", "some_account" });
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(expectedResult);
		
		// Act
		boolean success = mRepository.insertAccount(1, "some_account");
		
		// Assert
		Assert.assertEquals(expectedResult, success);
	}

}
