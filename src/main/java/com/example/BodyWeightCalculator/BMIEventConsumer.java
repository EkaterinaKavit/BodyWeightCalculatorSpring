package com.example.BodyWeightCalculator;


import com.example.BodyWeightCalculator.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BMIEventConsumer {

    private static Logger log = LoggerFactory.getLogger(BMIEventConsumer.class);

    @KafkaListener(topics="bmi-events", groupId="bmi-group")
    public void consume(Event event){
        log.info("ПОЛУЧЕНО СОБЫТИЕ: userId={}, resultId={}, bmi={}, category={}",
                event.getUserId(), event.getResultId(), event.getBmi(), event.getCategory());
    }
}
