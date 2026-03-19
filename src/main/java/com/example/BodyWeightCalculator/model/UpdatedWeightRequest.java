package com.example.BodyWeightCalculator.model;

public class UpdatedWeightRequest {
    double updatedWeight;

    public UpdatedWeightRequest() {
    }

    public UpdatedWeightRequest(double updatedWeight) {
        this.updatedWeight = updatedWeight;
    }

    public double getUpdatedWeight() {
        return updatedWeight;
    }

    public void setUpdatedWeight(double updatedWeight) {
        this.updatedWeight = updatedWeight;
    }
}
