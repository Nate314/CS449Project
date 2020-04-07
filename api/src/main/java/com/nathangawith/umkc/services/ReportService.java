package com.nathangawith.umkc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nathangawith.umkc.dtos.ReportRequest;
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
    public ReportRequest getReport(Date startDate, Date endDate, String breakpoint, String type) throws Exception {
		ReportRequest result = null;
		switch (type) {
			case "Category":
				result = mReportRepository.selectCategoryReport(startDate, endDate, breakpoint);
				break;
			case "Account":
				result = mReportRepository.selectAccountReport(startDate, endDate, breakpoint);
				break;
		}
		return result;
	}

}
