package com.nathangawith.umkc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @RequestMapping(
    		value = "/ping",
    		method = RequestMethod.GET
    )
    public ResponseEntity<Object> getPing()
    throws Exception {
    	return new ResponseEntity<Object>(
    			"Alive!",
    			HttpStatus.OK);
    }
}
