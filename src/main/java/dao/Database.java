package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:sqlite:crud_app.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection conn = getConnection()) {
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS entities (" +
                    "id TEXT PRIMARY KEY, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "createdAt TEXT, " +
                    "updatedAt TEXT)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
