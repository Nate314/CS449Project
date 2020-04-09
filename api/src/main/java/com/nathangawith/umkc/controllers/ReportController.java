package com.nathangawith.umkc.controllers;

import javax.servlet.http.HttpServletRequest;

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
import com.nathangawith.umkc.dtos.ReportResponse;
import com.nathangawith.umkc.interceptors.JWTInterceptor;
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
    		HttpServletRequest request,
    		@RequestBody ReportRequest body
    	) throws Exception {
		try {
			int userID = JWTInterceptor.getUserIDFromHeader(request);
			ReportResponse result = reportService.getReport(userID, body);
	    	String response = Algorithms.toJSONObject(result);
		    return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (Exception ex) {
			GenericResponse response = new GenericResponse();
			response.response = ex.getMessage();
			return new ResponseEntity<String>(Algorithms.toJSONObject(response), HttpStatus.NOT_FOUND);
		}
    }

}
