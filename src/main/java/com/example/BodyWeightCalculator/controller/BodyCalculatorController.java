package com.example.BodyWeightCalculator.controller;


import com.example.BodyWeightCalculator.model.*;
import com.example.BodyWeightCalculator.service.BodyWeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * REST-контроллер для управления замерами
 * Предоставляет эндпоинты для расчета индекса,для фильтрации результатов,для удаления замеров и для обновления информации
 */
@RestController
@RequestMapping("/api")
@Tag(name = "BodyCalculatorController", description = "API для управления замерами")
public class BodyCalculatorController {

    private final BodyWeightService service;
    private static final Logger log = LoggerFactory.getLogger(BodyCalculatorController.class);

    @Autowired
    public BodyCalculatorController(BodyWeightService service) {
        this.service = service;
    }

    /**
     * Вычисляет BMI и сохраняет результат для указанного пользователя.
     * @param requestWeight DTO с весом и ростом
     * @param userId идентификатор пользователя
     * @return созданный замер со статусом 201 Created
     */

    @PostMapping("/calculator")
    @Operation(summary = "Посчитать индекс массы тела", description = "Принимает вес,рост и идентификатор пользователя")

    public ResponseEntity<ResponseIndex> calculateBodyIndex(@Valid @RequestBody RequestWeight requestWeight, @RequestParam Long userId){
        log.info("Получен запрос на расчёт индекса: вес={}, рост={}", requestWeight.getWeight(), requestWeight.getHeight());
        ResponseIndex responseIndex = service.calculateIndex(requestWeight.getWeight(), requestWeight.getHeight(),userId);
        log.info("Рассчитан индекс id={}, значение={}", responseIndex.getId(), responseIndex.getIndex());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseIndex);
    }

    /**
     * Возвращает все замеры с пагинацией.
     * Параметры: page, size, sort (например, page=0&size=5&sort=date,desc).
     * @param pageable параметры пагинации и сортировки
     * @return страница с DTO замеров
     */
    @GetMapping("/getAll")
    @Operation(summary = "Показать все замеры", description = "Показывает все замеры с пагинацией")
    public Page<ResponseIndex> showResults(Pageable pageable){  //добавлена пагинация
        return service.getAllresults(pageable);
    }

    /**
     * Получение результатов по идентификатору
     * @param id
     * @return DTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить замер по идентификатору пользователя", description = "Принимает идентификатор пользователя")
    public ResponseIndex getById(@PathVariable Long id){
        log.info("Запрос данных по id={}", id);
        ResponseIndex dataById = service.getDataById(id);
        log.info("Данные найдены: id={}, индекс={}", dataById.getId(), dataById.getIndex());
        return dataById;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить замер по идентификатору пользователя", description = "Принимает идентификатор пользователя")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    @Operation(summary = "Удалить все замеры", description = "Будьте осторожны при удалении, это безвозвратно")
    public void deleteAll(){
        service.deleteAll();
    }



    @GetMapping("/statistics")
    @Operation(summary = "Получение данных по статистике замеров")
    public StatisticsData getStatistics(){
        return service.getStatistics();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить вес в замере по идентификатору пользователя", description = "Принимает идентификатор пользователя и новый показатель веса")
    public ResponseIndex updateWeightById(@PathVariable Long id, @RequestBody UpdatedWeightRequest updatedWeight){
        return service.updateWeightById(id,updatedWeight.getUpdatedWeight());
    }

    @GetMapping("/filter")
    @Operation(summary = "Фильтрация по категории", description = "Принимает название категории")
    public List<ResponseIndex> filterCategory(@RequestParam String newCategory){
        return service.filterByCategory(newCategory);
    }

    @GetMapping("/filter_for_date")
    @Operation(summary = "Фильтрация по дате", description = "Принимает дату для поиска")
    public List<ResponseIndex> filterDate(@RequestParam LocalDate dateForSearching){
        return  service.findByDate(dateForSearching);
    }

    @GetMapping("/filter_by_date_period")
    @Operation(summary = "Фильтрация по временному периоду", description = "Принимает начальную и конечную дату")
    public List<ResponseIndex> filterByPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        return  service.findByDateBetween(startDate,endDate);
    }

    @PutMapping("/{id}/height")
    @Operation(summary = "Обновить рост в замере по идентификатору пользователя", description = "Принимает идентификатор и новый показатель роста пользователя")
    public ResponseIndex updateHeightById(@PathVariable Long id, @RequestBody UpdatedHeightRequest newHeight){
        return service.updateHeightById(id,newHeight.getNewHeight());
    }


}
