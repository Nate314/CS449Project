package com.nathangawith.tests;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.controllers.ReportController;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.dtos.ReportResponse;
import com.nathangawith.umkc.dtos.Transaction;
import com.nathangawith.umkc.services.ReportService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerReportTest {

	@Mock
	private ReportService mService;
	
	@InjectMocks
	private ReportController mController = new ReportController();
	
	@Test
	public void postAddTransactionTest() {
		ReportRequest body = new ReportRequest();
		body.StartDate = new Date();
		body.EndDate = new Date();
		body.Breakpoint = "Month";
		body.Type = "Category";
		Transaction transaction = new Transaction();
		transaction.Date = new Date();
		transaction.Amount = 100.0;
		transaction.CategoryDescription = "SOME_CATEGORY_DESCRIPTION";
		ReportResponse expectedResult = new ReportResponse();
		expectedResult.StartDate = body.StartDate;
		expectedResult.EndDate = body.EndDate;
		expectedResult.Breakpoint = body.Breakpoint;
		expectedResult.Type = body.Type;
		expectedResult.Cells = new ArrayList<Transaction>();
		expectedResult.Cells.add(transaction);
		postReportGenericTest(false, "", body, expectedResult);
		postReportGenericTest(true, "SOME_EXCEPTION", body, expectedResult);
	}

	private void postReportGenericTest(boolean creationFailed, String exception, ReportRequest body, ReportResponse expectedResult) {
		try {
			// Arrange
			HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
			Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpc3MiOiJhdXRoMCIsInByZWZlcnJlZF91c2VybmFtZSI6InUiLCJleHAiOjE1ODI2MDU0MDIsImlhdCI6MTU4MjYwNDUwMn0.AVSbXFTQrUYII67oVfDhMJ3SzA22RgQXcDqlfIyKb00");
			ReportResponse result = new ReportResponse();
			result.StartDate = body.StartDate;
			result.EndDate = body.EndDate;
			result.Breakpoint = body.Breakpoint;
			result.Type = body.Type;
			result.Cells = expectedResult.Cells;
			
			if (!creationFailed) {
				Mockito.when(mService.getReport(1, body)).thenReturn(result);	
			} else {
				Mockito.when(mService.getReport(1, body)).thenThrow(new Exception(exception));
			}
			
			// Act
			ResponseEntity<String> response = mController.postReport(request, body);
			
			// Assert
			GenericResponse genericResponse = new GenericResponse();
			genericResponse.response = exception;
			Assert.assertEquals(response.getStatusCode(), creationFailed ? HttpStatus.NOT_FOUND : HttpStatus.OK);
			Assert.assertEquals(Algorithms.toJSONObject(creationFailed ? genericResponse : expectedResult), response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
