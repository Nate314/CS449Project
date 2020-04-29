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
		boolean success = true;
		if (isAccountTransfer) {
			success = success && mTransactionsRepository.insertTransaction(userID, fromID, -1, description, -1 * amount, date);
			success = success && mTransactionsRepository.insertTransaction(userID, toID, -1, description, amount, date);
		} else {
			success = success && mTransactionsRepository.insertTransaction(userID, -1, fromID, description, -1 * amount, date);
			success = success && mTransactionsRepository.insertTransaction(userID, -1, toID, description, amount, date);
		}
		return success && mTransactionsRepository.insertTransfer(userID, fromID, toID, isAccountTransfer, description, amount, date);
	}

    @Override
	public boolean editTransaction(int userID, int accountID, int categoryID, String description, double amount, Date date, int transactionID) {
		return mTransactionsRepository.updateTransaction(userID, accountID, categoryID, description, amount, date, transactionID);
	}

	@Override
	public boolean editTransfer(int userID, int fromID, int toID, boolean isAccountTransfer, String description, double amount, Date date, int transactionID) {
		return mTransactionsRepository.updateTransfer(userID, fromID, toID, isAccountTransfer, description, amount, date, transactionID);
	}

    @Override
	public boolean deleteTransaction(int userID, int transactionID) {
		return mTransactionsRepository.deleteTransaction(userID, transactionID);
	}

	@Override
	public boolean deleteTransfer(int userID, int transactionID) {
		return mTransactionsRepository.deleteTransfer(userID, transactionID);
	}
}
