package com.nathangawith.umkc.services;

import java.util.Date;

import com.nathangawith.umkc.dtos.ReportRequest;

public interface IReportService {
	public ReportRequest getReport(Date startDate, Date endDate, String breakpoint, String type) throws Exception;
}
