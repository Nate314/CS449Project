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
import com.nathangawith.umkc.repositories.AuthenticationRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryAuthenticationTest extends BaseTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private AuthenticationRepository mRepository = new AuthenticationRepository();

	@Test
	public void doesUsernameExistTest() {
		doesUsernameExistGenericTest(true);
		doesUsernameExistGenericTest(false);
	}

	@Test
	public void isValidLoginTest() {
		DBUser user = new DBUser();
		user.UserID = 1;
		user.Username = "u";
		isValidLoginGenericTest(user);
		isValidLoginGenericTest(null);
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
	
	private void isValidLoginGenericTest(DBUser expectedResult) {
		// Arrange
		String sql = Queries.GET_USER_PASSWORD;
		String hashedPass = Algorithms.encryptPassword("u", "p");
		List<String> params = Arrays.asList(new String[] {"u", hashedPass});
		Mockito.when(mDatabase.selectFirst(sql, params, DBUser.class)).thenReturn(expectedResult);

		// Act
		DBUser response = mRepository.isValidLogin("u", "p");
		
		// Assert
		if (expectedResult == null) {
			Assert.assertNull(response);
		} else {
			Assert.assertEquals(expectedResult.UserID, response.UserID);
			Assert.assertEquals(expectedResult.Username, response.Username);
		}
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
