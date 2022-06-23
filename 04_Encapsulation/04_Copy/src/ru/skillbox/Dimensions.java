package ru.skillbox;

public class Dimensions {
    private final int width;
    private final int height;
    private final int length;
    private final int volume;

    public Dimensions(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = length;
        volume = width * height * length;
    }

    public int getWidth() {
        return width;
    }

    public Dimensions setWidth(int width) {
        return new Dimensions(width, height, length);
    }

    public int getHeight() {
        return height;
    }

    public Dimensions setHeight(int height) {
        return new Dimensions(width, height, length);
    }

    public int getLength() {
        return length;
    }

    public Dimensions setLength(int length) {
        return new Dimensions(width, height, length);
    }

    //Получение объема груза
    public int getVolume() {
        return volume;
    }
}
