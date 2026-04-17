package com.example.BodyWeightCalculator.model;

public class UpdatedHeightRequest {

    double newHeight;

    public UpdatedHeightRequest() {
    }

    public UpdatedHeightRequest(double newHeight) {
        this.newHeight = newHeight;
    }

    public double getNewHeight() {
        return newHeight;
    }

    public void setNewHeight(double newHeight) {
        this.newHeight = newHeight;
    }
}
