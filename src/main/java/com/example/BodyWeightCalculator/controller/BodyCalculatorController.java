package com.example.BodyWeightCalculator.controller;


import com.example.BodyWeightCalculator.model.RequestWeight;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.service.BodyWeightService;
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
    public ResponseEntity<ResponseIndex> calculateBodyIndex(@RequestBody RequestWeight requestWeight){
        ResponseIndex responseIndex = service.calculateIndex(requestWeight.getWeight(), requestWeight.getHeight());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseIndex);
    }

    @GetMapping("/getAll")
    public List<ResponseIndex> showResults(){
        return new ArrayList<>(service.getAllresults());
    }
}
