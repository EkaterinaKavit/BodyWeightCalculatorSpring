//Используется как репозиторий для хранения в базе данных

package com.example.BodyWeightCalculator.jpa;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultJPARepository extends JpaRepository<ResultEntity, Long> {
}
