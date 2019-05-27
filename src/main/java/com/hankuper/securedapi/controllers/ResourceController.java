package com.hankuper.securedapi.controllers;

import com.hankuper.securedapi.entities.User;
import com.hankuper.securedapi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @Autowired
    private UserRepo userRepo;


    @GetMapping("/user-info")
    public User getUser(@RequestParam(defaultValue = "0") long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id - " + id));
    }
}
