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

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.DBUser;
import com.nathangawith.umkc.repositories.AccountRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryAccountTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private AccountRepository mRepository = new AccountRepository();

	@Test
	public void doesUsernameExistTest() {
		doesUsernameExistGenericTest(true);
		doesUsernameExistGenericTest(false);
	}

	@Test
	public void isValidLoginTest() {
		isValidLoginGenericTest(true);
		isValidLoginGenericTest(false);
	}
	
	@Test
	public void insertNewUserTest() {
		insertNewUserGenericTest(true);
		insertNewUserGenericTest(false);
	}
	
	private void doesUsernameExistGenericTest(boolean expectedResult) {
		// Arrange
		String sql = Queries.GET_USER;
		List<String> params = Arrays.asList(new String[] {"u"});
		DBUser user = new DBUser();
		user.Username = "u";
		Mockito.when(mDatabase.selectFirst(sql, params, DBUser.class)).thenReturn(expectedResult ? user : null);

		// Act
		boolean valid = mRepository.doesUsernameExist("u");
		
		// Assert
		Assert.assertEquals(expectedResult, valid);
	}
	
	private void isValidLoginGenericTest(boolean expectedResult) {
		// Arrange
		String sql = Queries.GET_USER_PASSWORD;
		String hashedPass = Algorithms.encryptPassword("u", "p");
		List<String> params = Arrays.asList(new String[] {"u", hashedPass});
		DBUser user = new DBUser();
		user.Username = "u";
		user.Password = hashedPass;
		Mockito.when(mDatabase.selectFirst(sql, params, DBUser.class)).thenReturn(expectedResult ? user : null);

		// Act
		boolean valid = mRepository.isValidLogin("u", "p");
		
		// Assert
		Assert.assertEquals(expectedResult, valid);
	}

	public void insertNewUserGenericTest(boolean expectedResult) {
		// Arrange
		String sql = Queries.INSERT_USER;
		String hashedPass = Algorithms.encryptPassword("u", "p");
		List<String> params = Arrays.asList(new String[] {"u", hashedPass});
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(expectedResult);
		
		// Act
		boolean success = mRepository.insertNewUser("u", "p");
		
		// Assert
		Assert.assertEquals(expectedResult, success);
	}

}
