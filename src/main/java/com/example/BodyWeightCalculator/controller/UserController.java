package com.example.BodyWeightCalculator.controller;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.entity.User;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.model.StatisticsData;
import com.example.BodyWeightCalculator.service.BodyWeightService;
import com.example.BodyWeightCalculator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Controller", description = "API для управления пользователями")
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
    @Operation(summary = "Создать нового пользователя", description = "Принимает данные пользователя и сохраняет его в базе данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<User> createUser(@RequestBody User user){
        User newUser = userService.createUser(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей")
    public List<User> gelAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по его идентификатору", description = "Принимает идентификатор пользователя")
    public ResponseEntity<User> getUserById(@PathVariable Long id){

        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);

    }

    @GetMapping("/{id}/results")
    @Operation(summary = "Получение все результатов пользователя по его идентификатору", description = "Принимает идентификатор пользователя")
    public List<ResponseIndex> getResultsByID(@PathVariable Long id){
       return  userService.getUserResultsById(id);

    }

    @DeleteMapping("/{id}/deleteResults")
    @Operation(summary = "Удаление всех данных пользователя по его идентификатору", description = "Принимает идентификатор пользователя")
    public void deleteAllByUserId(@PathVariable Long id){
        userService.deleteAllByUserId(id);
    }

    @GetMapping("/{id}/last-result")
    @Operation(summary = "Получение последнего замера пользователя по его идентификатору", description = "Принимает идентификатор пользователя")
    public ResponseIndex getLastResultByID(@PathVariable Long id){
        return  userService.findLastResult(id);

    }

    @GetMapping("/{id}/statistics")
    @Operation(summary = "Получение статистических данных пользователя по его идентификатору", description = "Принимает идентификатор пользователя")
    public StatisticsData getStatisticsByUserId(@PathVariable Long id){
        return  userService.getStatisticsDataByUserId(id);

    }


}
