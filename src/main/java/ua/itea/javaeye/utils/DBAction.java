package ua.itea.javaeye.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAction {
	// private String dbUrl = "jdbc:mysql://localhost/javaeye?";
	private Connection conn = null;
	private Statement st = null;

	protected Statement getSt() {
		return st;
	}

	// CREATE TABLE log (id INT PRIMARY KEY AUTO_INCREMENT, datetime VARCHAR(20),
	// path VARCHAR(200), action VARCHAR(20));

	public SQLException connect(String dbUrl, String user, String pwd) {
		try {
			String url = "jdbc:mysql://" + dbUrl + "/javaeye?";
			System.out.println("Establishing connection to " + url + "...");
			conn = DriverManager.getConnection(url, user, pwd);
			System.out.println("ok");
			System.out.println("Creating statement at " + url + "...");
			st = conn.createStatement();
			System.out.println("ok");
			return null;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return e;
		}
	}

	protected SQLException close() {
		try {
			st.close();
			System.out.println("Statement closed");
			conn.close();
			System.out.println("Connection closed");
			return null;
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			return e;
		}
	}
}
