package com.nathangawith.tests;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;

public class BaseTest {
	HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	boolean[] trueandfalse = new boolean[] {true, false};
	
	public BaseTest() {
		Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpc3MiOiJhdXRoMCIsInByZWZlcnJlZF91c2VybmFtZSI6InUiLCJleHAiOjE1ODI2MDU0MDIsImlhdCI6MTU4MjYwNDUwMn0.AVSbXFTQrUYII67oVfDhMJ3SzA22RgQXcDqlfIyKb00");
	}
}
