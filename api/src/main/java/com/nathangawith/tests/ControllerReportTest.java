package com.nathangawith.tests;

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
import com.nathangawith.umkc.services.ReportService;

@RunWith(MockitoJUnitRunner.class)
public class ControllerReportTest {

	@Mock
	private ReportService mService;
	
	@InjectMocks
	private ReportController mController = new ReportController();
	
	@Test
	public void postAddTransactionTest() {
		postReportGenericTest(false, "");
		postReportGenericTest(true, "SOME_EXCEPTION");
	}

	private void postReportGenericTest(boolean creationFailed, String exception) {
		try {
			// Arrange
			HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
			Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpc3MiOiJhdXRoMCIsInByZWZlcnJlZF91c2VybmFtZSI6InUiLCJleHAiOjE1ODI2MDU0MDIsImlhdCI6MTU4MjYwNDUwMn0.AVSbXFTQrUYII67oVfDhMJ3SzA22RgQXcDqlfIyKb00");
			ReportRequest body = new ReportRequest();
			body.StartDate = new Date();
			body.EndDate = new Date();
			body.Breakpoint = "Month";
			body.Type = "Category";

			if (!creationFailed) {
				Mockito.when(mService.getReport(body.StartDate, body.EndDate, body.Breakpoint, body.Type)).thenReturn(body);	
			} else {
				Mockito.when(mService.getReport(body.StartDate, body.EndDate, body.Breakpoint, body.Type)).thenThrow(new Exception(exception));
			}
			
			// Act
			ResponseEntity<String> response = mController.postReport(body);
			
			// Assert
			GenericResponse genericResponse = new GenericResponse();
			genericResponse.response = exception;
			Assert.assertEquals(response.getStatusCode(), creationFailed ? HttpStatus.NOT_FOUND : HttpStatus.OK);
			Assert.assertEquals(Algorithms.toJSONObject(creationFailed ? genericResponse : body), response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
