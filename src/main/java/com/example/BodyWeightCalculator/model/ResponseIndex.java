package com.example.BodyWeightCalculator.model;
import java.time.LocalDateTime;

public class ResponseIndex {

    private Long id;
    private double weight;
    private double height;
    private double index;
    private LocalDateTime date;

    public ResponseIndex() {
    }

    public ResponseIndex(Long id,double weight, double height,double index, LocalDateTime date) {
        this.weight = weight;
        this.height = height;
        this.id = id;
        this.index = index;
        this.date = date;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
