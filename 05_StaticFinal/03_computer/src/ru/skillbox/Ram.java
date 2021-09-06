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

    public Ram setRamType(RamType ramType) {
        return new Ram(ramType, ramSize, weight);
    }

    public RamSize getRamSize() {
        return ramSize;
    }

    public Ram setRamSize(RamSize ramSize) {
        return new Ram(ramType, ramSize, weight);
    }

    public int getWeight() {
        return weight;
    }

    public Ram setWeight (int weight) {
        return new Ram(ramType, ramSize, weight);
    }
}
