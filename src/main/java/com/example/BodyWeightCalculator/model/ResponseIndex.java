package com.example.BodyWeightCalculator.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResponseIndex {

    private Long id;
    private double weight;
    private double height;
    private double index;
    private LocalDate date;
    private String category;

    public ResponseIndex() {
    }

    public ResponseIndex(Long id,double weight, double height,double index, LocalDate date, String category) {
        this.weight = weight;
        this.height = height;
        this.id = id;
        this.index = index;
        this.date = date;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
