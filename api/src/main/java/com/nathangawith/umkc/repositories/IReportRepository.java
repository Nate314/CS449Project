package com.nathangawith.umkc.repositories;

import java.util.Date;

import com.nathangawith.umkc.dtos.ReportRequest;

public interface IReportRepository {
	public ReportRequest selectCategoryReport(Date startDate, Date endDate, String breakpoint) throws Exception;
	public ReportRequest selectAccountReport(Date startDate, Date endDate, String breakpoint) throws Exception;
}
