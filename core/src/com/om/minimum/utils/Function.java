package com.om.minimum.utils;

/**
 * Created by kongo on 07.05.16.
 */
public class Function {
    private String name;
    private String text;
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;

    public Function(String name, String text, float xMin, float xMax, float yMin, float yMax) {
        this.name = name;
        this.text = text;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getxMin() {
        return xMin;
    }

    public void setxMin(float xMin) {
        this.xMin = xMin;
    }

    public float getyMin() {
        return yMin;
    }

    public void setyMin(float yMin) {
        this.yMin = yMin;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }
}
