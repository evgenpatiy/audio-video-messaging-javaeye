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

import lombok.Getter;
import lombok.Setter;
import ua.itea.javaeye.ui.SessionListItem;

public class DbWorker {
    @Getter
    @Setter
    private String dbName;
    private File dbFile;
    private PreparedStatement pst;
    private ResultSet rs;

    public DbWorker(String dbName) {
        this.dbName = dbName;
        dbFile = new File(dbName);
    }

    public boolean isDbExists() {
        return dbFile.exists() && dbFile.isFile();
    }

    public void addSession(SessionListItem session) {
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

    public void editSession(SessionListItem session) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
            String sql = "UPDATE 'sessions' SET name=?, localaddress=?, remoteaddress=? WHERE id=?;";
            pst = conn.prepareStatement(sql);

            pst.setString(1, session.getRemoteName());
            pst.setString(2, session.getLocalAddress().getHostAddress());
            pst.setString(3, session.getRemoteAddress().getHostAddress());
            pst.setInt(4, session.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Writing data to database error", "SQL error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteSession(SessionListItem session) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
            String sql = "DELETE FROM 'sessions' WHERE id=?;";
            pst = conn.prepareStatement(sql);

            pst.setInt(1, session.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Deleting data from database error", "SQL error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public ArrayList<SessionListItem> getSessionsList() {
        ArrayList<SessionListItem> sessionsList = new ArrayList<SessionListItem>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName)) {
            String sql = "SELECT * FROM sessions;";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                SessionListItem session = new SessionListItem();

                session.setId(rs.getInt("id"));
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
