package ru.skillbox;

public class Country {
    private String name;
    private int population = 0;
    private double area = 0;
    private String capital = "";
    private boolean isAccessToSea = false;

    //Конструктор задающий название страны
    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public boolean isAccessToSea() {
        return isAccessToSea;
    }

    public void setAccessToSea(boolean accessToSea) {
        isAccessToSea = accessToSea;
    }
}
