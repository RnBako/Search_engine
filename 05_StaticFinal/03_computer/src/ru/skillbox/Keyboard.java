package ru.skillbox;

public class Keyboard {
    private final KeyboardType keyboardType;
    private final IsBacklight isBacklight;
    private final int weight;

    public Keyboard(KeyboardType keyboardType, IsBacklight isBacklight, int weight) {
        this.keyboardType = keyboardType;
        this.isBacklight = isBacklight;
        this.weight = weight;
    }

    public KeyboardType getKeyboardType() {
        return keyboardType;
    }

    public Keyboard setKeyboardType(KeyboardType keyboardType) {
        return new Keyboard(keyboardType, isBacklight, weight);
    }

    public IsBacklight getIsBacklight() {
        return isBacklight;
    }

    public Keyboard setIsBacklight(IsBacklight isBacklight) {
        return new Keyboard(keyboardType, isBacklight, weight);
    }

    public int getWeight() {
        return weight;
    }

    public Keyboard setWeight(int weight) {
        return new Keyboard(keyboardType, isBacklight, weight);
    }
}
