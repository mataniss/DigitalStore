package com.matan.api.controller;

import com.matan.api.model.User;
import com.matan.api.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @PostMapping("/signup")
    public Long signup(@RequestBody User user) throws SQLException {
        return usersRepository.userSignUp(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws SQLException {
        String token = usersRepository.login(user);
        return token;
    }

}