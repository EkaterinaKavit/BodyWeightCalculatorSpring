package com.example.BodyWeightCalculator.jpa;

import com.example.BodyWeightCalculator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью User
 * Использует стандартные CRUD-методы, а также дополнительные методы поиска
 * Наследуется от JPA - репозитория, что дает готовую реализацию методов
 */

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);


}
