package com.om.minimum.algorithm.genetic;

import com.om.minimum.Mapper;

/**
 * Created by kongo on 29.03.16.
 */
public class Chromosome implements Comparable<Chromosome> {
    private double x;
    private double y;
    private double z;

    public Chromosome(double y, double x) {
        this.y = y;
        this.x = x;
    }

    public void updateZ(Mapper mapper) {
        z = mapper.f(x, y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public int compareTo(Chromosome chromosome) {
        return Double.compare(z, chromosome.getZ());
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}
