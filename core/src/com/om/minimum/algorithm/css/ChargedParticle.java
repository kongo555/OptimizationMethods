package com.om.minimum.algorithm.css;

import com.om.minimum.Mapper;

/**
 * Created by kongo on 22.03.16.
 */
public class ChargedParticle implements Comparable<ChargedParticle> {
    private double x;
    private double y;
    private double z;
    private double q;
    private double vx;
    private double vy;

    public ChargedParticle(double x, double y) {
        this.x = x;
        this.y = y;
        vx = 0;
        vy = 0;

    }

    public void updateZ(Mapper mapper) {
        z = mapper.f(x, y);
    }

    public void updateQ(double fWorst, double fBest) {
        q = (z - fWorst) / (fBest - fWorst);
    }

    @Override
    public int compareTo(ChargedParticle other) {
        return Double.compare(z, other.getZ());
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

    public void setZ(double z) {
        this.z = z;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}
