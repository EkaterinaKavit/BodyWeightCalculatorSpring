package com.example.BodyWeightCalculator.service;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.entity.User;
import com.example.BodyWeightCalculator.exceptions.ResourceNotFoundException;
import com.example.BodyWeightCalculator.jpa.UserRepository;
import com.example.BodyWeightCalculator.model.ResponseIndex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        log.info("Создание пользователя: {}", user.getEmail());
        return userRepository.save(user);
    }

    public User getUserById(Long id){
       return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Пользователь с id "+id+" не найден"));

    }

    public List<User> getAllUsers(){
        log.debug("Запрос всех пользователей");
        return  userRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<ResponseIndex> getUserResultsById(Long id) {
        User user = getUserById(id);
        List<ResponseIndex> resultList = new ArrayList<>();
        for (ResultEntity result : user.getResults()) {
            ResponseIndex dto = new ResponseIndex(
                    result.getId(),
                    result.getWeight(),
                    result.getHeight(),
                    result.getIndex(),
                    result.getDate(),
                    result.getCategory()
            );
            resultList.add(dto);
        }
        return resultList;
    }
}
