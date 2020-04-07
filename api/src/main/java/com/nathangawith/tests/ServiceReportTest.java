package com.nathangawith.tests;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.repositories.IReportRepository;
import com.nathangawith.umkc.services.ReportService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceReportTest {

	@Mock
	private IReportRepository mRepository;
	
	@InjectMocks
	private ReportService mService = new ReportService(mRepository);

	@Test
	public void getReportTest() {
		ReportRequest request = new ReportRequest();
		request.StartDate = new Date();
		request.EndDate = new Date();
		request.Breakpoint = "Month";
		request.Type = "Account";
		getReportGenericTest(request, request);
		request.Type = "Category";
		getReportGenericTest(request, request);
	}
	
	private void getReportGenericTest(ReportRequest input, ReportRequest expectedResult) {
		try {
			// Arrange
			boolean setup = false;
			if (input.Type == "Account") {
				setup = true;
				Mockito.when(mRepository.selectAccountReport(input.StartDate, input.EndDate, input.Breakpoint)).thenReturn(expectedResult);
			} else if (input.Type == "Category") {
				setup = true;
				Mockito.when(mRepository.selectCategoryReport(input.StartDate, input.EndDate, input.Breakpoint)).thenReturn(expectedResult);
			}
			Assert.assertTrue(setup);
		
			// Act
			ReportRequest result = mService.getReport(input.StartDate, input.EndDate, input.Breakpoint, input.Type);
			
			// Assert
			Assert.assertEquals(expectedResult, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
	}
}
