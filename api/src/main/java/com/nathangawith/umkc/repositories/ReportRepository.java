package com.nathangawith.umkc.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.Transaction;

@Component("report_repository")
public class ReportRepository implements IReportRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;

	@Override
	public Collection<Transaction> selectBreakpointCategoryReport(int userID, Date startDate, Date endDate, String breakpoint, String type) {
		String year = "Year", month = "Month", category = "Category", account = "Account";
		System.out.println(breakpoint);
		System.out.println(type);
		System.out.println(breakpoint.equals(year));
		System.out.println(breakpoint.equals(month));
		System.out.println(type.equals(category));
		System.out.println(type.equals(account));
		String group_by =
			breakpoint.equals(year)
			? (type.equals(category)
				? Queries.GET_REPORT_GROUP_BY_YEAR_CATEGORY
				: (type.equals(account)
					? Queries.GET_REPORT_GROUP_BY_YEAR_ACCOUNT
					: null))
			: (breakpoint.equals(month)
				? (type.equals(category)
					? Queries.GET_REPORT_GROUP_BY_MONTH_CATEGORY
					: (type.equals(account)
						? Queries.GET_REPORT_GROUP_BY_MONTH_ACCOUNT
						: null))
				: null);
		String sql = Queries.GET_REPORT_SELECT_FROM_WHERE + group_by;
		String sd = Algorithms.dateToString(startDate), ed = Algorithms.dateToString(endDate);
		List<String> params = Algorithms.params(userID, sd, ed);
		Collection<Transaction> result = DB.select(sql, params, Transaction.class);
		return result;
	}

}
