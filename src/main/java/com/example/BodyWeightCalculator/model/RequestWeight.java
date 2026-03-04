package com.example.BodyWeightCalculator.model;

public class RequestWeight {
    private double weight;
    private double height;

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
