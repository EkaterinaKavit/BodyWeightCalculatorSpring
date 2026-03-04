package com.example.BodyWeightCalculator.repository;

import com.example.BodyWeightCalculator.model.ResponseIndex;

import java.time.LocalDateTime;
import java.util.List;

public interface BodyWeightInterface {

     ResponseIndex save(double weight, double height, double index, LocalDateTime date);
     List<ResponseIndex> showAllResults();
}
