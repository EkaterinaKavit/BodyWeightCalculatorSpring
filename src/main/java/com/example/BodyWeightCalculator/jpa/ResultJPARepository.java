//Используется как репозиторий для хранения в базе данных

package com.example.BodyWeightCalculator.jpa;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResultJPARepository extends JpaRepository<ResultEntity, Long> {

    List<ResultEntity> findByCategory(String category);
    List<ResultEntity> findByDate(LocalDate date);
    List<ResultEntity> findByDateBetween(LocalDate start, LocalDate end);

}
