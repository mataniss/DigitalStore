package com.matan.api.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteJDBC {
    public static final String dbURL = "jdbc:sqlite:myDB.db";
    public static Connection con;

    public static void connectToDB() {
        try {
            // create a connection to the database
            con = DriverManager.getConnection(dbURL);
            System.out.println("Connection with db was created successfully");
        } catch (SQLException ex) {
           System.err.println(ex.toString());
        }
    }

    public static ResultSet executeQuery(String sqlQuery) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        st = con.createStatement();
        rs = st.executeQuery(sqlQuery);
        return rs;

    }

    public static int executeUpdateSQL(String sqlQuery) throws SQLException
    {
        int returnedRow = -1;
        Statement st = con.createStatement();
        returnedRow = st.executeUpdate(sqlQuery);
        return returnedRow;
    }

    public static void closeDBConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }


    public static void main(String[] args) throws SQLException {
        connectToDB();
        ResultSet results= executeQuery("SELECT * FROM PRODUCTS");
        System.out.println("done");
    }
}
