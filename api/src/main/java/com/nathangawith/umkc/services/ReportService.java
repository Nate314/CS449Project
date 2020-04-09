package com.nathangawith.umkc.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.dtos.ReportResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.repositories.IReportRepository;

@Component("report_service")
public class ReportService implements IReportService {

    private IReportRepository mReportRepository;

    @Autowired
    public ReportService(
        @Qualifier("report_repository")
        IReportRepository aReportRepository
    ) {
        this.mReportRepository = aReportRepository;
    }

	@Override
    public ReportResponse getReport(int userID, ReportRequest request) throws Exception {
		Date sd = request.StartDate, ed = request.EndDate;
		String bp = request.Breakpoint, type = request.Type;
		Collection<Transaction> cells = mReportRepository.selectBreakpointCategoryReport(userID, sd, ed, bp, type);
		ReportResponse result = new ReportResponse();
		result.Cells = (List<Transaction>) cells;
		result.StartDate = sd;
		result.EndDate = ed;
		result.Breakpoint = bp;
		result.Type = type;
		return result;
	}

}
