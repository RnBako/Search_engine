package ru.skillbox;

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

    public MonitorType getMonitorType() {
        return monitorType;
    }

    public int getWeight() {
        return weight;
    }
}
