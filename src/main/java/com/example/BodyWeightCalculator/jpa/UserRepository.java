package com.example.BodyWeightCalculator.jpa;

import com.example.BodyWeightCalculator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

}
