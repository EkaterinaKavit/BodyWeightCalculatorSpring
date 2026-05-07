package com.example.BodyWeightCalculator.service;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.entity.User;
import com.example.BodyWeightCalculator.exceptions.ResourceNotFoundException;
import com.example.BodyWeightCalculator.jpa.ResultJPARepository;
import com.example.BodyWeightCalculator.jpa.UserRepository;
import com.example.BodyWeightCalculator.model.ResponseIndex;
import com.example.BodyWeightCalculator.model.StatisticsData;
import com.example.BodyWeightCalculator.model.UpdatedHeightRequest;
import com.example.BodyWeightCalculator.repository.BodyWeightRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.BodyWeightCalculator.model.Event;


import javax.xml.transform.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервисный слой для бизнес-логики, связанной с замерами
 * Взаимодействует с ResultJPARepository для работы с базой данных и UserService для получения информации о пользователях
 */
@Service
public class BodyWeightService {

    private final ResultJPARepository repository;
    private final UserService userService;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String BMI_TOPIC = "bmi-events";

    /**
     * Логгер для записи событий в файл и консоль
     */
    private static final Logger log = LoggerFactory.getLogger(BodyWeightService.class);


    @Autowired
    public BodyWeightService(ResultJPARepository repository, UserService userService, KafkaTemplate<String,Object> kafkaTemplate) {
        this.repository = repository;
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     *  Вычисляет индекс, категорию и сохраняет замер для пользователя
     * @param weight вес в килограммах
     * @param height рост в метрах
     * @param userID идентификатор пользователя, которому принадлежит замер
     * @return DTO(data transfer object) с сохраненным замером
     */
    public ResponseIndex calculateIndex(double weight, double height, Long userID){
        log.debug("Вычисление индекса для weight={}, height={}, user={}", weight, height,userID);
        //User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("Не найден пользователь с id " + userID));
        User user = userService.getUserById(userID);
        double index = weight/(height*height);
        LocalDate date = LocalDate.now();
        String category = determineCategory(index);
        log.debug("Индекс={}, категория={}", index, category);

        //Создаем сущность
        ResultEntity entity = new ResultEntity(weight,height,index,category,date,user);

        //Сохраняем в БД
        ResultEntity saved = repository.save(entity);
        log.info("Сохранён результат id={} для пользователя {}", saved.getId(), user.getName());
        log.debug("DTO сформирован: id={}", saved.getId());

        Event event = new Event(user.getId(),saved.getId(),saved.getIndex(), saved.getCategory(), saved.getDate());
        kafkaTemplate.send(BMI_TOPIC, event);
        log.debug("Событие отправлено в Kafka: {}", event);

        //преобразуем в ResponseIndex для ответа
        return new ResponseIndex(saved.getId(), saved.getWeight(), saved.getHeight(), saved.getIndex(), saved.getDate(), saved.getCategory());
    }

    /**
     * Возвращает страницу всех замеров с пагинацией
     * @param pageable  параметры пагинации (количество результатов на странице, номер страницы)
     * @return страница с DTO замеров
     */
    public Page<ResponseIndex> getAllresults(Pageable pageable){   //добавлена пагинация
        log.debug("Запрос всех записей из БД");
        Page <ResultEntity> entitiesPage = repository.findAll(pageable);
        Page<ResponseIndex> page = entitiesPage
                .map(e -> new ResponseIndex(e.getId(), e.getWeight(), e.getHeight(), e.getIndex(),
                        e.getDate(), e.getCategory()));
        log.debug("Получено {} записей (страница {})", page.getNumberOfElements(), page.getNumber());
        return page;
    }

    /**
     * Возвращает информацию по идентификатору пользователя
     * @param id идентификатор пользователя
     * @return DTO c замерами
     */

    public ResponseIndex getDataById(Long id){
         log.debug("Поиск записи по id={}", id);
         ResultEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Запись с id " + id + " не найдена"));

         return new ResponseIndex(entity.getId(), entity.getWeight(), entity.getHeight(), entity.getIndex(), entity.getDate(), entity.getCategory());
    }

    /**
     * Удаление записи по идентификатору
     * @param id идентификатор пользователя
     * если идентификато не найден, то выбрасывается исключение о несуществующей записи
     */
    public void deleteById(Long id){
        log.debug("Удаление записи по id={}", id);

        if (!repository.existsById(id)){
            log.warn("Попытка удалить несуществующую запись id={}", id);
            throw new ResourceNotFoundException("Запись с id "+ id+" не найдена");
        }
        repository.deleteById(id);
        log.info("Запись id={} успешно удалена", id);
    }



    /**
     * удаление всех записей
     */

    public void deleteAll(){
        log.debug("Удаление всех записей");
        repository.deleteAll();
        log.info("Все записи удалены");
    }


    /**
     * Получение статистики: минимальный, максимальный вес,средний индекс, количество замеров
     * @return возвращает обьект класса StatisticsData, содержащий вычисленные значения
     */

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

    /**
     * Обновление веса по идентификатору пользователя, пересчитывает индес и категорию
     * @param id идентификатор пользователя
     * @param newWeight новый вес в килограммах
     * @return возвращает DTO с обновленными данными
     * @throws ResourceNotFoundException, если запись с таким id не найдена
     */

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


    /**
     * Обновление роста по идентификатору пользователя, пересчитывает индес и категорию
     * @param id идентификатор пользователя
     * @param newHeight новый рост в метрах
     * @return возвращает DTO с обновленными данными
     * @throws ResourceNotFoundException, если запись с таким id не найдена
     */
    public ResponseIndex updateHeightById(Long id, double newHeight){
        ResultEntity updated = repository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Записьа с id "+id+" не найдена"));
        updated.setHeight(newHeight);
        double updatedIndex = updated.getWeight()/ (updated.getHeight()* updated.getHeight());
        String updatedCategory = determineCategory(updatedIndex);
        updated.setIndex(updatedIndex);
        updated.setCategory(updatedCategory);
        ResultEntity updatedHeight = repository.save(updated);
        return new ResponseIndex(updated.getId(), updated.getWeight(),updated.getHeight(),updated.getIndex(),updated.getDate(),updated.getCategory());

    }

    /**
     * Определение категории по индексу
     * @param index
     * @return возвращает категорию
     */
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

    /**
     * Сортировка по категории(точное совпадение)
     * @param newCategory
     * @return возвращает список DTO, имеющих искомую категорию
     */
    public List<ResponseIndex> filterByCategory(String newCategory){
        log.debug("Фильтрация по категории: '{}'", newCategory);
        List<ResultEntity> entities = repository.findByCategory(newCategory);
        log.debug("Найдено сущностей: {}", entities.size());
        List<ResponseIndex> results = new ArrayList<>();
        for (ResultEntity e:entities){
            results.add(new ResponseIndex(e.getId(), e.getWeight(), e.getHeight(),e.getIndex(), e.getDate(),e.getCategory()));

        }
        log.debug("Возвращаем DTO: {}", results.size());
        return results;
    }

    /**
     * Сортировка по дате
     * @param dateForSearching дата для поиска
     * @return список DTO, имеющих искомую дату
     */

    public List<ResponseIndex> findByDate(LocalDate dateForSearching){
        List<ResultEntity> entities = repository.findByDate(dateForSearching);
        List<ResponseIndex> resultsOfSearching = new ArrayList<>();
        for (ResultEntity e:entities){
            resultsOfSearching.add(new ResponseIndex(e.getId(),e.getWeight(),e.getHeight(),e.getIndex(),e.getDate(),e.getCategory()));
        }
        return  resultsOfSearching;
    }


    /**
     * Фильтрация  по временному периоду
     * @param start начало диапазона
     * @param end конец диапазона
     * @return возвращает список DTO, подходящих под указанный период
     */
    public List<ResponseIndex> findByDateBetween(LocalDate start, LocalDate end){
        List<ResultEntity> entities = repository.findByDateBetween(start,end);
        List<ResponseIndex> resultsOfSearching = new ArrayList<>();
        for (ResultEntity e:entities){
            resultsOfSearching.add(new ResponseIndex(e.getId(),e.getWeight(),e.getHeight(),e.getIndex(),e.getDate(),e.getCategory()));
        }
        return  resultsOfSearching;
    }


}
