package com.example.BodyWeightCalculator.controller;


import com.example.BodyWeightCalculator.model.RequestWeight;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.model.StatisticsData;
import com.example.BodyWeightCalculator.service.BodyWeightService;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BodyCalculatorController {

    private final BodyWeightService service;

    @Autowired
    public BodyCalculatorController(BodyWeightService service) {
        this.service = service;
    }

    @PostMapping("/calculator")
    public ResponseEntity<ResponseIndex> calculateBodyIndex(@Valid @RequestBody RequestWeight requestWeight){
        ResponseIndex responseIndex = service.calculateIndex(requestWeight.getWeight(), requestWeight.getHeight());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseIndex);
    }

    @GetMapping("/getAll")
    public List<ResponseIndex> showResults(){
        return new ArrayList<>(service.getAllresults());
    }

    @GetMapping("/{id}")
    public ResponseIndex getById(@PathVariable Long id){
        ResponseIndex dataById = service.getDataById(id);
        return dataById;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll(ServletRequest servletRequest){
        service.deleteAll();
    }

    @GetMapping("/statistics")
    public StatisticsData getStatistics(){
        return service.getStatistics();
    }
}
