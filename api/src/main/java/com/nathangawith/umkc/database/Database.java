package com.nathangawith.umkc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.nathangawith.umkc.Config;

@Component("my_database")
public class Database implements IDatabase {

	public Database() {
	}

	@Override
	public <T extends Object> ArrayList<T> select(String sql, List<String> params, Class<T> type) {
		ArrayList<T> objs = new ArrayList<T>();
		try {
			Connection conn = this.getConnection();
			ResultSet rs = this.ex(conn, sql, params, false);
			JSONArray json = new JSONArray();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				int numColumns = rsmd.getColumnCount();
				JSONObject jsonOBJ = new JSONObject();
				for (int i = 1; i <= numColumns; i++) {
					String column_name = rsmd.getColumnName(i);
					jsonOBJ.put(column_name, rs.getObject(column_name));
				}
				json.put(jsonOBJ);
			}
			for (int i = 0; i < json.length(); i++)
				objs.add(new Gson().fromJson(json.get(i).toString(), type));
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objs;
	}

	@Override
	public <T extends Object> T selectFirst(String sql, List<String> params, Class<T> type) {
		ArrayList<T> objs = this.select(sql, params, type);
		if (objs.size() > 0) {
			return objs.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean execute(String sql, List<String> params) {
		try {
			Connection conn = this.getConnection();
			this.ex(conn, sql, params, true);
			conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private ResultSet ex(Connection conn, String sql, List<String> params, boolean dml) throws SQLException {
		int paramIndex = 0;
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		for (String param : params)
			prepStmt.setString(++paramIndex, param);
		if (dml) {
			prepStmt.execute();
			return null;
		} else
			return prepStmt.executeQuery();
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		String conn = Config.dbConnectionURL, usr = Config.dbUser, pass = Config.dbPassword;
		return DriverManager.getConnection(conn, usr, pass);
	}

}
