package com.example.BodyWeightCalculator.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="results")
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double weight;
    private double height;
    private double index;
    private String category;
    private LocalDate date;

    public ResultEntity() {
    }

    public ResultEntity(double weight, double height, double index, String category, LocalDate date) {
        this.weight = weight;
        this.height = height;
        this.index = index;
        this.category = category;
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
