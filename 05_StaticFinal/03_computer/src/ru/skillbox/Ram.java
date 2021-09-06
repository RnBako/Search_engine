package ru.skillbox;

public class Ram {
    private final RamType ramType;
    private final RamSize ramSize;
    private final int weight;

    public Ram(RamType ramType, RamSize ramSize, int weight) {
        this.ramType = ramType;
        this.ramSize = ramSize;
        this.weight = weight;
    }

    public RamType getRamType() {
        return ramType;
    }

    public RamSize getRamSize() {
        return ramSize;
    }

    public int getWeight() {
        return weight;
    }
}
