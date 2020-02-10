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
	public void isValidLoginTest() {
		isValidLoginGenericTest(true);
		isValidLoginGenericTest(false);
	}
	
	@Test
	public void insertNewUserTest() {
		insertNewUserGenericTest(true);
		insertNewUserGenericTest(false);
	}
	
	private void isValidLoginGenericTest(boolean expectedResult) {
		// Arrange
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
		String hashedPass = this.hashAndSalt("p");
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
		String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
		String hashedPass = this.hashAndSalt("p");
		List<String> params = Arrays.asList(new String[] {"u", hashedPass});
		Mockito.when(mDatabase.execute(sql, params)).thenReturn(expectedResult);
		
		// Act
		boolean success = mRepository.insertNewUser("u", "p");
		
		// Assert
		Assert.assertEquals(expectedResult, success);
	}

	private String hashAndSalt(String password) {
		System.out.println("Hashing and salting . . . ");
		System.out.println(password);
		String modifiedPassword = "";
		for (char c : password.toCharArray()) modifiedPassword += c + "n";
		System.out.println(modifiedPassword);
		String result = "";
		for (char c : modifiedPassword.toCharArray()) result += c + 15;
		System.out.println(result);
		System.out.println(" . . . Hashed and salted");
		return result;
	}

}
