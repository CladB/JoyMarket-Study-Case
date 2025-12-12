package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connect {
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "joymarketDB";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	private Connection conn;
	private Statement st;
	private static Connect instance;

	public static Connect getInstance() {
		if (instance == null) {
			instance = new Connect();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}

	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = conn.createStatement();
			System.out.println("Database Connected: " + DATABASE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection Failed!");
		}
	}

	public ResultSet execQuery(String query) {
		try {
			st = conn.createStatement();
			return st.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public PreparedStatement prepareStatement(String query) {
		try {
			return conn.prepareStatement(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}