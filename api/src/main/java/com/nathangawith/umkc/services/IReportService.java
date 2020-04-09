package com.nathangawith.umkc.services;

import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.dtos.ReportResponse;

public interface IReportService {
	public ReportResponse getReport(int userID, ReportRequest request) throws Exception;
}
