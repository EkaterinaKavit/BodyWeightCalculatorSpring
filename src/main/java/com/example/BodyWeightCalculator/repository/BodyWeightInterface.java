package com.example.BodyWeightCalculator.repository;

import com.example.BodyWeightCalculator.model.ResponseIndex;

import java.time.LocalDate;
import java.util.List;

public interface BodyWeightInterface {

     ResponseIndex save(double weight, double height, double index, LocalDate date, String category);
     List<ResponseIndex> showAllResults();
     ResponseIndex getById(Long id);
     void deleteById(Long id);
     void deleteAll();
}
