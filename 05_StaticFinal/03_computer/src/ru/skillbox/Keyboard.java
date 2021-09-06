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

    public IsBacklight getIsBacklight() {
        return isBacklight;
    }

    public int getWeight() {
        return weight;
    }
}
