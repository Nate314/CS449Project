package com.nathangawith.umkc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.repositories.ITransactionsRepository;

@Component("transactions_service")
public class TransactionsService implements ITransactionsService {

    private ITransactionsRepository mTransactionsRepository;

    @Autowired
    public TransactionsService(
        @Qualifier("transactions_repository")
        ITransactionsRepository aTransactionsRepository
    ) {
        this.mTransactionsRepository = aTransactionsRepository;
    }

    @Override
	public boolean addNewTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date) {
		return mTransactionsRepository.insertTransaction(userID, accountID, categoryID, description, amount, date);
	}

	@Override
	public boolean addNewTransfer(int userID, int fromID, int toID, boolean isAccountTransfer, String description, double amount, Date date) {
		return mTransactionsRepository.insertTransfer(userID, fromID, toID, isAccountTransfer, description, amount, date);
	}
}
