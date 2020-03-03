package com.nathangawith.umkc.database;

import java.util.Collection;
import java.util.List;

public interface IDatabase {
	public <T extends Object> Collection<T> select     (String sql, List<String> params, Class<T> type);
	public <T extends Object> T             selectFirst(String sql, List<String> params, Class<T> type);
	public                    boolean       execute    (String sql, List<String> params);
}
