package com.matan.api.repository;

import com.matan.api.managers.DBManager;
import com.matan.api.model.Product;
import com.matan.api.model.User;
import com.matan.api.utils.Utils;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class UsersRepository {
    public Long userSignUp(User user) throws SQLException {
        Long id = null;
        if(getUserByUsername(user.getUsername()) == null) {
            String encryptedPassword = Utils.encryptPassword(user.getPassword());
            String sql = "INSERT INTO USERS (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = DBManager.getDBConnection().prepareStatement(sql);

            // Set parameters for the query
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, encryptedPassword);
            pstmt.setString(3, user.getEmail());

            // Execute the insert operation
            id = DBManager.performInsertAndGetGeneratedID(pstmt);
        }
        else {
            throw new Error("Username already exists");
        }
        return id;
    }

    public User getUserByID(Long id) throws SQLException {
        User user = null;
        String sqlStatement = String.format("SELECT * FROM USERS WHERE id = %s", id);
        ArrayList<User>  users = listUsers(sqlStatement);
        if(users.size() > 0){
            user  = users.get(0);
        }
    return  user;
    }

    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        String sqlStatement = String.format("SELECT * FROM USERS WHERE username = '%s';", username);
        ArrayList<User>  users = listUsers(sqlStatement);
        if(users.size() > 0){
            user  = users.get(0);
        }
        return  user;
    }


    public ArrayList<User> listUsers() throws SQLException {
        return listUsers("SELECT * FROM USERS") ;
    }

    public ArrayList<User> listUsers(String sqlStatement) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        ResultSet rs = DBManager.executeQuery(sqlStatement);
        while (rs.next()) {
            Long id =  rs.getLong("id");
            String name = rs.getString("username");
            String hashedPassword = rs.getString("password");
            String email = rs.getString("email");
            User newUser = new User(id, name, hashedPassword, email);
            users.add(newUser);
        }
        return users;

    }

    public String login(User user) throws SQLException {
        String token = null;
        User userInDB = getUserByUsername(user.getUsername());
        if(userInDB != null) {
            if(Utils.validatePassword(user.getPassword(),userInDB.getPassword())){
                token = Utils.generateToken(userInDB.getId());
            }
            else {
                throw new Error("Incorrect password");
            }
        }
        else {
            throw new Error("Invalid username or password");
        }
        return token;
    }
}