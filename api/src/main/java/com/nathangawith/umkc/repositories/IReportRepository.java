package com.nathangawith.umkc.repositories;

import java.util.Collection;
import java.util.Date;

import com.nathangawith.umkc.dtos.Transaction;

public interface IReportRepository {
	public Collection<Transaction> selectBreakpointCategoryReport(int userID, Date startDate, Date endDate, String breakpoint, String type) throws Exception;
}
