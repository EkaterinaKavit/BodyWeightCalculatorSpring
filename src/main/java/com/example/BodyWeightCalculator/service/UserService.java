package com.example.BodyWeightCalculator.service;

import com.example.BodyWeightCalculator.entity.ResultEntity;
import com.example.BodyWeightCalculator.entity.User;
import com.example.BodyWeightCalculator.exceptions.ResourceNotFoundException;
import com.example.BodyWeightCalculator.jpa.ResultJPARepository;
import com.example.BodyWeightCalculator.jpa.UserRepository;
import com.example.BodyWeightCalculator.model.ResponseIndex;

import com.example.BodyWeightCalculator.model.StatisticsData;
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
    private final ResultJPARepository resultRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);



    @Autowired
    public UserService(UserRepository userRepository, ResultJPARepository resultRepository) {
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
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

    @Transactional
    public void deleteAllByUserId(Long userId){
        if (!userRepository.existsById(userId)){
            log.warn("Попытка удалить несуществующую запись id={}", userId);
            throw new ResourceNotFoundException("Пользователь с id "+ userId+" не найден");
        }
        resultRepository.deleteAllByUserId(userId);
    }


    @Transactional(readOnly = true)
    public ResponseIndex findLastResult(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден");
        }
        ResultEntity lastResult = resultRepository.findFirstByUserIdOrderByDateDesc(userId).orElseThrow(() ->
                new ResourceNotFoundException("У пользователя с id " + userId + " нет замеров"));
        return new ResponseIndex(lastResult.getId(), lastResult.getWeight(), lastResult.getHeight(),
                lastResult.getIndex(), lastResult.getDate(),lastResult.getCategory());

    }

    @Transactional(readOnly = true)
    public StatisticsData getStatisticsDataByUserId(Long userId){
        userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));

        long totalResults = resultRepository.countByUserId(userId);
        if (totalResults==0){
            return new StatisticsData(0.0,0.0,0.0,0);
        }

        Double avgIndex = resultRepository.avgIndexByUserId(userId).orElse(0.0);
        Double minWeight = resultRepository.minWeightByUserId(userId).orElse(0.0);
        Double maxWeight = resultRepository.maxWeightByUserId(userId).orElse(0.0);

        return new StatisticsData(avgIndex,minWeight,maxWeight,(int) totalResults);



    }
}
