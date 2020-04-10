package com.nathangawith.umkc.financeapp.dtos;

import java.util.List;

public class ReportResponseDto {
    public String StartDate;
    public String EndDate;
    public String Breakpoint;
    public String Type;
    public List<TransactionDto> Cells;
}
