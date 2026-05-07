package com.example.BodyWeightCalculator.model;

import java.time.LocalDate;

public class Event {

    private Long userId;
    private Long resultId;
    private double bmi;
    private String category;
    private LocalDate date;

    public Event() {
    }

    public Event(Long userId, Long resultId, double bmi, String category, LocalDate date) {
        this.userId = userId;
        this.resultId = resultId;
        this.bmi = bmi;
        this.category = category;
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
