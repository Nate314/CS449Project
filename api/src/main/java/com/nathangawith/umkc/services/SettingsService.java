package com.nathangawith.umkc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Messages;
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
	public boolean addAccount(int userID, String description) throws Exception {
		if (mSettingsRepository.doesAccountExist(userID, description))
			throw new Exception(Messages.ACCOUNT_ALREADY_EXISTS);
		else
			return mSettingsRepository.insertAccount(userID, description);
	}
}
