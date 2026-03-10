package com.example.BodyWeightCalculator.service;

import com.example.BodyWeightCalculator.exceptions.ResourceNotFoundException;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.model.StatisticsData;
import com.example.BodyWeightCalculator.repository.BodyWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        LocalDate date = LocalDate.now();
        String category;
        if (index<18.5){
            category = "дефицит массы тела";}
        else if  (index<25){
            category ="нормальный вес";}
        else if  (index<30){
            category ="повышенный вес";}
        else if  (index<35){
            category ="1-я стадия ожирения";}
        else if  (index<40){
            category ="2-я стадия ожирения";}
        else{
            category ="чрезвычайно высокий вес";
        }

        return bodyWeightRepository.save(weight,height,index,date, category);
    }

    public List<ResponseIndex> getAllresults(){
        return bodyWeightRepository.showAllResults();
    }

    public ResponseIndex getDataById(Long id){
        ResponseIndex dataById = bodyWeightRepository.getById(id);
        return dataById;
    }

    public void deleteById(Long id){
        ResponseIndex existData= bodyWeightRepository.getById(id);
        if (existData==null){
            throw new ResourceNotFoundException("Запись с id "+ id+" не найдена");
        }
        bodyWeightRepository.deleteById(id);
    }

    public void deleteAll(){
        bodyWeightRepository.deleteAll();
    }

    public StatisticsData getStatistics(){
        List<ResponseIndex> results = bodyWeightRepository.showAllResults();
        double sumIndex=0;
        double minWeight = Double.MAX_VALUE;
        double maxWeight = Double.MIN_VALUE;

        for (ResponseIndex r:results) {
            if (r.getWeight() < minWeight) {
                minWeight = r.getWeight();
            }

            if (r.getWeight() > maxWeight) {
                maxWeight = r.getWeight();
            }

            sumIndex+=r.getIndex();

        }

        double averageIndex = sumIndex/results.size();
        return new StatisticsData(averageIndex,minWeight,maxWeight, results.size());

    }
}
