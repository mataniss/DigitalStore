package com.matan.api.managers;

import java.sql.*;

public class DBManager {
    public static final String dbURL = "jdbc:sqlite:serverDB.db";
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
    public static Connection getDBConnection(){
        return con;
    }


    public static void deleteRowById(String table, Long id) throws SQLException {
        executeUpdateSQL(String.format("DELETE FROM %S WHERE id = %s",table,id));
    }

    //todo: write a function that removes an item by id from db

    public static void main(String[] args) {
        connectToDB();
        ResultSet rs;
        try{
            rs = executeQuery("SELECT * FROM PRODUCTS");

            System.out.println("!!!!");
        }
        catch (Exception e){
            System.err.println(e.toString());
        }
        System.out.println("done");
    }
}
