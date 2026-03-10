package com.example.BodyWeightCalculator.model;

public class StatisticsData {
    private double averageIndex;
    private double minWeight;
    private double maxWeight;
    private long totalCount;

    public StatisticsData() {
    }

    public StatisticsData(double averageIndex, double minWeight, double maxWeight, long totalCount) {
        this.averageIndex = averageIndex;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.totalCount = totalCount;
    }

    public double getAverageIndex() {
        return averageIndex;
    }

    public void setAverageIndex(double averageIndex) {
        this.averageIndex = averageIndex;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(double minWeight) {
        this.minWeight = minWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
