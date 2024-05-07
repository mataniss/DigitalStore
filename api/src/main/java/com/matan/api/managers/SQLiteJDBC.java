package com.matan.api.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteJDBC {
    public static void queryDatabase() {
        String url = "jdbc:sqlite:myDB.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to the SQLite database.");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tablename;");

            while (rs.next()) {
                System.out.println(rs.getInt("column1") +  "\t" +
                        rs.getString("column2") + "\t" +
                        rs.getDouble("column3"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        queryDatabase();
        System.out.println("query finished");
    }
}
