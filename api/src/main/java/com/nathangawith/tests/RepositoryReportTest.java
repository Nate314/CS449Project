package com.nathangawith.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.Queries;
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.dtos.ReportResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.repositories.ReportRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryReportTest extends BaseTest {
	
	class ReportRequestResponse {
		ReportRequest request;
		ReportResponse response;
	}

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private ReportRepository mRepository = new ReportRepository();
	
	private ReportRequestResponse getRequestAndExpectedResult(String breakpoint, String type) {
		ReportRequest request = new ReportRequest();
		request.StartDate = new Date();
		request.EndDate = new Date();
		request.Breakpoint = breakpoint;
		request.Type = type;
		Transaction transaction = new Transaction();
		transaction.Date = new Date();
		transaction.Amount = 100.0;
		transaction.AccountDescription = type == "Account" ? "SOME_ACCOUNT_DESCRIPTION" : null;
		transaction.CategoryDescription = type == "Category" ? "SOME_CATEGORY_DESCRIPTION" : null;
		ReportResponse expectedResult = new ReportResponse();
		expectedResult.StartDate = request.StartDate;
		expectedResult.EndDate = request.EndDate;
		expectedResult.Breakpoint = request.Breakpoint;
		expectedResult.Type = request.Type;
		expectedResult.Cells = new ArrayList<Transaction>();
		expectedResult.Cells.add(transaction);
		ReportRequestResponse result = new ReportRequestResponse();
		result.request = request;
		result.response = expectedResult;
		return result;
	}

	@Test
	public void selectCategoryReport() {
		ReportRequestResponse requestAndExpectedResult = getRequestAndExpectedResult("Year", "Account");
		ReportRequest request = requestAndExpectedResult.request;
		ReportResponse expectedResult = requestAndExpectedResult.response;
		selectReportGenericTest(true, true, request, expectedResult.Cells);
	}
	
	private void selectReportGenericTest(boolean year, boolean account, ReportRequest request, List<Transaction> expectedResult) {
		// Arrange
		String sql = Queries.GET_REPORT_SELECT_FROM_WHERE;
		sql += year ? (account ? Queries.GET_REPORT_GROUP_BY_YEAR_ACCOUNT
				: Queries.GET_REPORT_GROUP_BY_YEAR_CATEGORY)
			: (account ? Queries.GET_REPORT_GROUP_BY_MONTH_ACCOUNT
				: Queries.GET_REPORT_GROUP_BY_MONTH_CATEGORY);
		String sd = Algorithms.dateToString(request.StartDate), ed = Algorithms.dateToString(request.EndDate);
		String bp = request.Breakpoint, type = request.Type;
		List<String> params = Algorithms.params(1, sd, ed);
		ReportResponse result = new ReportResponse();
		result.StartDate = request.StartDate;
		result.EndDate = request.EndDate;
		result.Breakpoint = request.Breakpoint;
		result.Type = type;
		Mockito.when(mDatabase.select(sql, params, Transaction.class)).thenReturn(expectedResult);

		// Act
		Collection<Transaction> response = mRepository.selectBreakpointCategoryReport(1, request.StartDate, request.EndDate, bp, type);

		// Assert
		Assert.assertEquals(expectedResult, (List<Transaction>) response);
	}

}
