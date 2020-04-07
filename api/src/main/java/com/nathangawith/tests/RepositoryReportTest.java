package com.nathangawith.tests;

import java.util.ArrayList;
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
import com.nathangawith.umkc.database.IDatabase;
import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.repositories.ReportRepository;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryReportTest {

	@Mock
	private IDatabase mDatabase;

	@InjectMocks
	private ReportRepository mRepository = new ReportRepository();
	
	private List<ReportRequest> getRequestAndExpectedResult(String type) {
		ReportRequest request = new ReportRequest(), expectedResult = new ReportRequest();
		request.StartDate = new Date();
		expectedResult.StartDate = new Date();
		request.EndDate = new Date();
		expectedResult.EndDate = new Date();
		request.Breakpoint = "Month";
		expectedResult.Breakpoint = "Month";
		request.Type = null;
		expectedResult.Type = type;
		List<ReportRequest> result = new ArrayList<ReportRequest>();
		result.add(request);
		result.add(expectedResult);
		return result;
	}

	@Test
	public void selectCategoryReport() {
		List<ReportRequest> requestAndExpectedResult = getRequestAndExpectedResult("Category");
		ReportRequest request = requestAndExpectedResult.get(0);
		ReportRequest expectedResult = requestAndExpectedResult.get(1);
		selectReportGenericTest(true, request, expectedResult);
	}

	@Test
	public void selectAccountReport() {
		List<ReportRequest> requestAndExpectedResult = getRequestAndExpectedResult("Account");
		ReportRequest request = requestAndExpectedResult.get(0);
		ReportRequest expectedResult = requestAndExpectedResult.get(1);
		selectReportGenericTest(false, request, expectedResult);
	}
	
	private void selectReportGenericTest(boolean category, ReportRequest request, ReportRequest expectedResult) {
		// Arrange
		String sql = ReportRepository.dummySQL;
		String sd = Algorithms.dateToString(request.StartDate), ed = Algorithms.dateToString(request.EndDate);
		String bp = request.Breakpoint, type = category ? "Category" : "Account";
		List<String> params = Algorithms.params(sd, ed, bp, type);
		ReportRequest result = new ReportRequest();
		result.StartDate = request.StartDate;
		result.EndDate = request.EndDate;
		result.Breakpoint = request.Breakpoint;
		result.Type = type;
		Mockito.when(mDatabase.selectFirst(sql, params, ReportRequest.class)).thenReturn(result);

		// Act
		ReportRequest response = null;
		if (category) {
			response = mRepository.selectCategoryReport(request.StartDate, request.EndDate, bp);
		} else {
			response = mRepository.selectAccountReport(request.StartDate, request.EndDate, bp);
		}
		
		// Assert
		Assert.assertEquals(expectedResult.StartDate, response.StartDate);
		Assert.assertEquals(expectedResult.EndDate, response.EndDate);
		Assert.assertEquals(expectedResult.Breakpoint, response.Breakpoint);
		Assert.assertEquals(expectedResult.Type, response.Type);
	}

}
