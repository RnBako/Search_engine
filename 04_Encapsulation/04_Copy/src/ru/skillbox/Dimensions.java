package ru.skillbox;

public class Dimensions {
    private final int width;
    private final int height;
    private final int length;

    public Dimensions(int width, int height, int lenght) {
        this.width = width;
        this.height = height;
        this.length = lenght;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    //Получение объема груза
    public int getVolume() {
        return (width * height * length);
    }
}
