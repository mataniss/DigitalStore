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

    public static Long performInsertAndGetGeneratedID(PreparedStatement pstmt) throws SQLException {
        Long generatedID = null;
        ResultSet generatedKeys = null;
        // Execute the insert operation
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedID = generatedKeys.getLong(1); // Retrieve the first field of the generated keys (typically the ID)
                System.out.println("Insert successful, generatedID: " + generatedID);
            } else {
                System.out.println("Insert successful, but no ID was returned.");
            }
        } else {
            throw new Error("Insert failed, no rows affected.");
        }
        return generatedID;
    }


    public static void deleteRowById(String table, Long id) throws SQLException {
        executeUpdateSQL(String.format("DELETE FROM %S WHERE id = %s",table,id));
    }

    public static void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
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
