package com.nathangawith.umkc.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.repositories.IRegisterRepository;


@Component("register_service")
public class RegisterService implements IRegisterService {

    private IRegisterRepository mRegisterRepository;

    @Autowired
    public RegisterService(
        @Qualifier("register_repository")
        IRegisterRepository aRegisterRepository
    ) {
        this.mRegisterRepository = aRegisterRepository;
    }

	@Override
	public Collection<Transaction> getTransactions(int userID) throws Exception {
		Collection<Transaction> transactions = mRegisterRepository.selectTransactions(userID);
		for (Transaction transaction : transactions)
			transaction.IsIncome = transaction.Amount >= 0;
		return transactions;
	}
	
	@Override
	public String getTotal(int userID) throws Exception {
		double total = mRegisterRepository.selectTotal(userID);
		return Algorithms.formatAsMoney(total);
	}
}
