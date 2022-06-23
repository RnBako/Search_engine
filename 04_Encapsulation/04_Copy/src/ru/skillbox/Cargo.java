package ru.skillbox;

public class Cargo {
    private final Dimensions dimensions;
    private final int weight;
    private final String deliveryAddress;
    private final boolean isOverTurn;
    private final String registrationNumber;
    private final boolean isFragile;

    public Cargo(Dimensions dimensions, int weight, String deliveryAddress, boolean isOverTurn, String registrationNumber, boolean isFragile) {
        this.dimensions = dimensions;
        this.weight = weight;
        this.deliveryAddress = deliveryAddress;
        this.isOverTurn = isOverTurn;
        this.registrationNumber = registrationNumber;
        this.isFragile = isFragile;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public Cargo setDimensions(Dimensions dimensions) {
        return new Cargo(dimensions, weight,deliveryAddress, isOverTurn, registrationNumber, isFragile);
    }

    public int getWeight() {
        return weight;
    }

    public Cargo setWeight(int weight) {
        return new Cargo(dimensions, weight,deliveryAddress, isOverTurn, registrationNumber, isFragile);
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Cargo setDeliveryAddress(String deliveryAddress) {
        return new Cargo(dimensions, weight,deliveryAddress, isOverTurn, registrationNumber, isFragile);
    }

    public boolean isOverTurn() {
        return isOverTurn;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public boolean isFragile() {
        return isFragile;
    }
}
