package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	private static final String HOST = "localhost:3306";
	private static final String DATABASE = "delivery_system_db";
	private static String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection connection;
	private static Connect conn;
	
	private Connect() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Berhasil connect database");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Gagal connect dengan database");
		}
	}
	
	public static Connect getInstance() {
		if(conn == null) {
			conn = new Connect();
		} 
		return conn;
		
	}
	
	public Connection getConnection() {
		return this.connection;
	}
}
