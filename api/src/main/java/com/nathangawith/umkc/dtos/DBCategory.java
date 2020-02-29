package com.nathangawith.umkc.dtos;

public class DBCategory {
	public int CategoryID;
	public int UserID;
	public int TypeID;
	public String Description;

    public int getCategoryID() { return CategoryID; }
    public int getUserID() { return UserID; }
    public int getTypeID() { return TypeID; }
    public String getDescription() { return Description; }
}
