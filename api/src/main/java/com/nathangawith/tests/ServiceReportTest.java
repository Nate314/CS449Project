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

import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.dtos.ReportResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.repositories.IReportRepository;
import com.nathangawith.umkc.services.ReportService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceReportTest {
	
	class ReportRequestResponse {
		ReportRequest request;
		ReportResponse response;
	}

	@Mock
	private IReportRepository mRepository;
	
	@InjectMocks
	private ReportService mService = new ReportService(mRepository);

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
		selectReportGenericTest(true, true, request, expectedResult);
	}
	
	private void selectReportGenericTest(boolean year, boolean account, ReportRequest request, ReportResponse expectedResult) {
		try {
			// Arrange
			Date sd = request.StartDate, ed = request.EndDate;
			String bp = request.Breakpoint, type = request.Type;
			ReportResponse result = new ReportResponse();
			result.StartDate = request.StartDate;
			result.EndDate = request.EndDate;
			result.Breakpoint = request.Breakpoint;
			result.Type = type;
			Mockito.when(mRepository.selectBreakpointCategoryReport(1, sd, ed, bp, type)).thenReturn(expectedResult.Cells);

			// Act
			Collection<Transaction> response = mRepository.selectBreakpointCategoryReport(1, request.StartDate, request.EndDate, bp, type);
			result.Cells = (List<Transaction>) response;

			// Assert
			Assert.assertEquals(expectedResult.StartDate, result.StartDate);
			Assert.assertEquals(expectedResult.EndDate, result.EndDate);
			Assert.assertEquals(expectedResult.Breakpoint, result.Breakpoint);
			Assert.assertEquals(expectedResult.Type, result.Type);
			Assert.assertEquals(expectedResult.Cells, result.Cells);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
