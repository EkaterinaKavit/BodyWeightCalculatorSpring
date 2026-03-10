package com.example.BodyWeightCalculator.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RequestWeight {

    @NotNull(message ="Вес не может быть пустым")
    @Min(value=1, message = "Вес должен быть больше 0")
    @Max(value=250, message = "Вес должен быть меньше 250")

    private Double weight;

    @NotNull(message ="Рост не может быть пустым")
    @Min(value=1, message = "Рост должен быть больше 0")
    @Max(value=250, message = "Рост должен быть меньше 250")
    private Double height;

    public RequestWeight() {
    }

    public RequestWeight(double height, double weight) {
        this.height = height;
        this.weight = weight;
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
}
