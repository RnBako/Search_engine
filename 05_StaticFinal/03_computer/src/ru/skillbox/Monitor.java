package ru.skillbox;

import java.nio.charset.MalformedInputException;

public class Monitor {
    private final Diagonal diagonal;
    private final MonitorType monitorType;
    private final int weight;

    public Monitor(Diagonal diagonal, MonitorType monitorType, int weight) {
        this.diagonal = diagonal;
        this.monitorType = monitorType;
        this.weight = weight;
    }

    public Diagonal getDiagonal() {
        return diagonal;
    }

    public Monitor setDiagonal(Diagonal diagonal) {
        return new Monitor(diagonal, monitorType, weight);
    }

    public MonitorType getMonitorType() {
        return monitorType;
    }

    public Monitor setMonitorType(MonitorType monitorType) {
        return new Monitor(diagonal, monitorType, weight);
    }

    public int getWeight() {
        return weight;
    }

    public Monitor setWeight(int weight) {
        return new Monitor(diagonal, monitorType, weight);
    }
}
