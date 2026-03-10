package com.example.BodyWeightCalculator.repository;


import com.example.BodyWeightCalculator.model.RequestWeight;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BodyWeightRepository implements BodyWeightInterface{

    private final ConcurrentHashMap<Long, ResponseIndex> results = new ConcurrentHashMap<>();
    AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public ResponseIndex save(double weight, double height, double index, LocalDate date, String category){
        Long id = idGenerator.getAndIncrement();
        ResponseIndex dataOfPerson = new ResponseIndex(id,weight, height, index,date,category);
        results.put(id,dataOfPerson);
        return dataOfPerson;
    }

    @Override
    public List<ResponseIndex> showAllResults() {
        return new ArrayList<>(results.values());
    }

    @Override
    public ResponseIndex getById(Long id) {
        return results.get(id) ;
    }

    @Override
    public void deleteById(Long id) {
        results.remove(id);
    }

    @Override
    public void deleteAll() {
        results.clear();
    }
}
