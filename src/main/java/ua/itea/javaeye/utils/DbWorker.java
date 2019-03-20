package ua.itea.javaeye.utils;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DbWorker {
	private String dbName;
	private File dbFile;
	private PreparedStatement pst;
	private ResultSet rs;

	public DbWorker(String dbName) {
		this.dbName = dbName;
		dbFile = new File(dbName);
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public boolean isDbExists() {
		return dbFile.exists() && dbFile.isFile();
	}

	public void addSession(Session session) {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
			String sql = "INSERT INTO 'sessions' (name, localaddress, remoteaddress) VALUES (?, ?, ?);";
			pst = conn.prepareStatement(sql);

			pst.setString(1, session.getRemoteName());
			pst.setString(2, session.getLocalAddress().getHostAddress());
			pst.setString(3, session.getRemoteAddress().getHostAddress());
			pst.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Writing data to database error", "SQL error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public int getMaxId() {
		int max = 0;
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
			String sql = "SELECT MAX(id) FROM sessions;";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			max = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return max;
	}

	public ArrayList<Session> getSessionsList() {
		ArrayList<Session> sessionsList = new ArrayList<Session>();

		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
			String sql = "SELECT * FROM sessions;";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {
				Session session = new Session();

				session.setRemoteName(rs.getString("name"));
				session.setLocalAddress(InetAddress.getByName(rs.getString("localaddress")));
				session.setRemoteAddress(InetAddress.getByName(rs.getString("remoteaddress")));
				sessionsList.add(session);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Read from database error!", "SQL error", JOptionPane.ERROR_MESSAGE);
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Unknown host!", "Database error", JOptionPane.ERROR_MESSAGE);
		}

		return sessionsList;
	}

	public void createDb() {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
			String sql = "CREATE TABLE if not exists 'sessions' (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(20), localaddress VARCHAR(20), remoteaddress VARCHAR(20));";
			pst = conn.prepareStatement(sql);
			pst.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Database creation error!", "SQL error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
