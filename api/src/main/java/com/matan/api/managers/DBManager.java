package com.matan.api.managers;

import java.sql.*;
/*This class manages the connection with the db and provides function that
    performs queries in the db.
 */
public class DBManager {
    //this variable defines the filename of the db. If it doesn't exist,
    //it'll be created automatically
    public static final String dbURL = "jdbc:sqlite:serverDB.db";
    public static Connection con;

    /*
    The function performs that first connection to the db, and must be called at least once
    in the server application lifecycle
     */
    public static void connectToDB() {
        try {
            // create a connection to the database
            con = DriverManager.getConnection(dbURL);
            System.out.println("Connection with db was created successfully");
        } catch (SQLException ex) {
           System.err.println(ex.toString());
        }
    }
    /*
    The function gets a string that represents a sql query and returns a result set
    with the records that were received when performing this sql query.
    This function doesn't allow to perform changes in the db, only pull data from it.
     */
    public static ResultSet executeQuery(String sqlQuery) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        st = con.createStatement();
        rs = st.executeQuery(sqlQuery);
        return rs;

    }
    /*
    The function get a string that represents an update sql query, and returns an integer
    that returns the number of records that were created in the db during this operation.
     */
    public static int executeUpdateSQL(String sqlQuery) throws SQLException
    {
        int returnedRow = -1;
        Statement st = con.createStatement();
        returnedRow = st.executeUpdate(sqlQuery);
        return returnedRow;
    }
    /*
    This function closes the connection to db.
     */
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
    /*
    The function gets a PreparedStatement for a sql insert query and returns the id that was generated
    automatically by the DB to the first record that was created.
     */
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

    /*
    The function gets a table and an id in this table, and removes the record with the id that
    was provided from the table in the db.
     */
    public static void deleteRowById(String table, Long id) throws SQLException {
        executeUpdateSQL(String.format("DELETE FROM %S WHERE id = %s",table,id));
    }
}
