package com.excilys.formation.tbezenger.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import com.excilys.formation.tbezenger.Strings;
import com.excilys.formation.tbezenger.exceptions.DAO.ConnectionException;
import com.excilys.formation.tbezenger.exceptions.DAO.DatabaseException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariProxyConnection;

public class ConnectionManager {
	private static ConnectionManager instance;
    public static ConnectionManager getInstance() {
        if (instance == null) {
           instance = new ConnectionManager();
        }
        return instance;
    }
    private ConnectionManager() { }

    HikariDataSource ds;

    public Connection openConnection() throws DatabaseException {
    	if (ds == null) {
    		openDataSource();
    	}
		HikariProxyConnection conn = null;
		try {
			conn = (HikariProxyConnection) ds.getConnection();
		} catch (SQLException e) {
			throw (new ConnectionException());
		}
		return conn;
	}

	public HikariDataSource openDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(Strings.url);
		config.setUsername(Strings.dbName);
		config.setPassword(Strings.dbPassword);
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "256");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.setMaximumPoolSize(64);
		config.setMinimumIdle(5);
		ds = new HikariDataSource(config);
		return ds;
	}
}
