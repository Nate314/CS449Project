package com.nathangawith.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Messages;
import com.nathangawith.umkc.repositories.ISettingsRepository;
import com.nathangawith.umkc.services.SettingsService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceSettingsTest {

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
}
