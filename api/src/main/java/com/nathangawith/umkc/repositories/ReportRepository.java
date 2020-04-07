package com.nathangawith.umkc.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.ReportRequest;

@Component("report_repository")
public class ReportRepository implements IReportRepository {

	@Autowired
	@Qualifier("my_database")
	IDatabase DB;
	
	public static String dummySQL = "SELECT ? AS StartDate, ? AS EndDate, ? AS Breakpoint, ? AS Type";

	@Override
	public ReportRequest selectCategoryReport(Date startDate, Date endDate, String breakpoint) {
		String sd = Algorithms.dateToString(startDate), ed = Algorithms.dateToString(endDate);
		List<String> params = Algorithms.params(sd, ed, breakpoint, "Category");
		ReportRequest result = DB.selectFirst(dummySQL, params, ReportRequest.class);
		return result;
	}
	
	@Override
	public ReportRequest selectAccountReport(Date startDate, Date endDate, String breakpoint) {
		String sd = Algorithms.dateToString(startDate), ed = Algorithms.dateToString(endDate);
		List<String> params = Algorithms.params(sd, ed, breakpoint, "Account");
		ReportRequest result = DB.selectFirst(dummySQL, params, ReportRequest.class);
		return result;
	}

}
