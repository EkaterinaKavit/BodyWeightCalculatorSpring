//Используется как репозиторий для хранения в базе данных

package com.example.BodyWeightCalculator.jpa;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью ResultEntity
 * Предоставляет CRUD-методы, а также кастомные методы поиска
 *
 */
@Repository
public interface ResultJPARepository extends JpaRepository<ResultEntity, Long> {

    /**
     *Находит все замеры пользователя по категории
     * Возвращает список замеров
     */
    List<ResultEntity> findByCategory(String category);
    /**
     *Находит все замеры пользователя по дате
     * Возвращает список замеров
     */
    List<ResultEntity> findByDate(LocalDate date);
    /**
     *Находит все замеры пользователя по временному промежутку
     * Возвращает список замеров
     */
    List<ResultEntity> findByDateBetween(LocalDate start, LocalDate end);

    void deleteAllByUserId(Long id);

    Optional<ResultEntity> findFirstByUserIdOrderByDateDesc(Long userId);

    long countByUserId (Long userId);

    @Query("SELECT AVG(r.index) FROM ResultEntity r WHERE r.user.id= :userId")
    Optional<Double> avgIndexByUserId(@Param("userId") Long userId);

    @Query("SELECT MIN(r.weight) FROM ResultEntity r WHERE r.user.id= :userId")
    Optional<Double> minWeightByUserId(@Param("userId") Long userId);

    @Query("SELECT MAX(r.weight) FROM ResultEntity r WHERE r.user.id= :userId")
    Optional<Double> maxWeightByUserId(@Param("userId") Long userId);




}
