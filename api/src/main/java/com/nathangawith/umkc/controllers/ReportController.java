package com.nathangawith.umkc.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nathangawith.umkc.Algorithms;
import com.nathangawith.umkc.dtos.GenericResponse;
import com.nathangawith.umkc.dtos.ReportRequest;
import com.nathangawith.umkc.services.IReportService;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    @Qualifier("report_service")
    private IReportService reportService;

    @RequestMapping(value = "/report",
    		method = RequestMethod.POST
    )
    public ResponseEntity<String> postReport(
    		@RequestBody ReportRequest request
    	) throws Exception {
		try {
			Date sd = request.StartDate, ed = request.EndDate;
			String bp = request.Breakpoint, type = request.Type;
	    	ReportRequest result = reportService.getReport(sd, ed, bp, type);
	    	String response = Algorithms.toJSONObject(result);
		    return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (Exception ex) {
			GenericResponse response = new GenericResponse();
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
    }

}
