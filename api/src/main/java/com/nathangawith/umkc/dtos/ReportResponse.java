package com.nathangawith.umkc.dtos;

import java.util.Date;
import java.util.List;

public class ReportResponse {
	public Date StartDate;
	public Date EndDate;
	public String Breakpoint;
	public String Type;
	public List<Transaction> Cells;
}
