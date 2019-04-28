package com.example.tattooshopapp.model;

import java.util.Date;

public class ForecastItem {

    private String description;
    private String location;
    private double temperature;
    private double pressure;
    private double humidity;
    private Date date;

    public ForecastItem(String description, String location, double temperature, double pressure, double humidity) {
        this.description = description;
        this.location = location;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.date = new Date();
    }

    public ForecastItem(String description, String location, double temp) {
        this.description = description;
        this.location = location;
        this.temperature = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ForecastItem{" +
                "description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}
