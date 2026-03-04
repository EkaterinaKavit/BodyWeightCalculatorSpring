package com.example.BodyWeightCalculator.service;

import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.repository.BodyWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BodyWeightService {
    private final BodyWeightRepository bodyWeightRepository;

    @Autowired

    public BodyWeightService(BodyWeightRepository bodyWeightRepository) {
        this.bodyWeightRepository = bodyWeightRepository;
    }

    public ResponseIndex calculateIndex(double weight, double height){
        double index = weight/(height*height);
        LocalDateTime date = LocalDateTime.now();
        return bodyWeightRepository.save(weight,height,index,date);
    }

    public List<ResponseIndex> getAllresults(){
        return bodyWeightRepository.showAllResults();
    }
}
