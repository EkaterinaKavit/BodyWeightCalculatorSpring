package com.example.BodyWeightCalculator.service;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.exceptions.ResourceNotFoundException;
import com.example.BodyWeightCalculator.jpa.ResultJPARepository;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.model.StatisticsData;
import com.example.BodyWeightCalculator.repository.BodyWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.xml.transform.Result;
import java.time.LocalDate;
import java.util.List;


@Service
public class BodyWeightService {
    private final ResultJPARepository repository;
    private static final Logger log = LoggerFactory.getLogger(BodyWeightService.class);

    @Autowired
    public BodyWeightService(ResultJPARepository repository) {
        this.repository = repository;
    }

    public ResponseIndex calculateIndex(double weight, double height){
        log.debug("Вычисление индекса для weight={}, height={}", weight, height);
        double index = weight/(height*height);
        LocalDate date = LocalDate.now();
        String category = determineCategory(index);
        log.debug("Индекс={}, категория={}", index, category);

        //Создаем сущность
        ResultEntity entity = new ResultEntity(weight,height,index,category,date);

        //Сохраняем в БД
        ResultEntity saved = repository.save(entity);
        log.info("Сохранён результат id={}", saved.getId());
        log.debug("DTO сформирован: id={}", saved.getId());

        //преобразуем в ResponseIndex для ответа
        return new ResponseIndex(saved.getId(), saved.getWeight(), saved.getHeight(), saved.getIndex(), saved.getDate(), saved.getCategory());
    }

    public List<ResponseIndex> getAllresults(){
        log.debug("Запрос всех записей из БД");
        List<ResponseIndex> results = repository.findAll().stream()
                .map(e -> new ResponseIndex(e.getId(), e.getWeight(), e.getHeight(), e.getIndex(),
                        e.getDate(), e.getCategory())).toList();
        log.debug("Получено {} записей", results.size());
        return results;
    }

    public ResponseIndex getDataById(Long id){
         log.debug("Поиск записи по id={}", id);
         ResultEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Запись с id " + id + " не найдена"));

         return new ResponseIndex(entity.getId(), entity.getWeight(), entity.getHeight(), entity.getIndex(), entity.getDate(), entity.getCategory());
    }

    public void deleteById(Long id){
        log.debug("Удаление записи по id={}", id);

        if (!repository.existsById(id)){
            log.warn("Попытка удалить несуществующую запись id={}", id);
            throw new ResourceNotFoundException("Запись с id "+ id+" не найдена");
        }
        repository.deleteById(id);
        log.info("Запись id={} успешно удалена", id);
    }

    public void deleteAll(){
        log.debug("Удаление всех записей");
        repository.deleteAll();
        log.info("Все записи удалены");
    }

    public StatisticsData getStatistics(){

        log.debug("Вычисление статистики");

        List<ResultEntity> results = repository.findAll();
        double sumIndex=0;
        double minWeight = Double.MAX_VALUE;
        double maxWeight = Double.MIN_VALUE;

        for (ResultEntity r:results) {
            if (r.getWeight() < minWeight) {
                minWeight = r.getWeight();
            }

            if (r.getWeight() > maxWeight) {
                maxWeight = r.getWeight();
            }

            sumIndex+=r.getIndex();

        }

        double averageIndex = sumIndex/results.size();

        log.debug("Статистика: средний индекс={}, мин.вес={}, макс.вес={}, всего={}",
                averageIndex, minWeight, maxWeight, results.size());
        return new StatisticsData(averageIndex,minWeight,maxWeight, results.size());

    }

    public ResponseIndex updateWeightById(Long id, double newWeight){
        ResultEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Запись с id " + id + " не найдена"));
        entity.setWeight(newWeight);
        double updatedIndex = newWeight/(entity.getHeight()*entity.getHeight());
        String updatedCategory = determineCategory(updatedIndex);
        entity.setIndex(updatedIndex);
        entity.setCategory(updatedCategory);
        ResultEntity updated = repository.save(entity);
        return new ResponseIndex(updated.getId(),updated.getWeight(),updated.getHeight(),updated.getIndex(),updated.getDate(),updated.getCategory());

    }

    public static String determineCategory(double index){
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

        return category;
    }
}
