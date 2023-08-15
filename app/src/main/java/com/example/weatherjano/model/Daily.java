package com.example.weatherjano.model;


public class Daily {

    private Integer dt;
    private Double day;
    private Double min;
    private Double max;
    private String description;
    private String icon;

    public Daily(Integer dt, Double day, Double min, Double max, String description, String icon) {
        this.dt = dt;
        this.day = day;
        this.min = min;
        this.max = max;
        this.description = description;
        this.icon = icon;
    }


    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }
    public Double getDay() {
        return day;
    }

    public void setDay(Double day) {
        this.day = day;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
