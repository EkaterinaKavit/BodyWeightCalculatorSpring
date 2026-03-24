package com.example.BodyWeightCalculator.controller;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.entity.User;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.service.BodyWeightService;
import com.example.BodyWeightCalculator.service.UserService;
import jakarta.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final BodyWeightService service;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(UserService userService, BodyWeightService service) {
        this.userService = userService;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User newUser = userService.createUser(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping
    public List<User> gelAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){

        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);

    }

    @GetMapping("/{id}/results")
    public List<ResponseIndex> getResultsByID(@PathVariable Long id){
       return  userService.getUserResultsById(id);

    }


}
