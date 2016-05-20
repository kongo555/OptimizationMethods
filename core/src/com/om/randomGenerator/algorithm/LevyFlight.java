package com.om.randomGenerator.algorithm;

import com.badlogic.gdx.math.MathUtils;
import com.om.minimum.utils.Point;

/**
 * Created by kongo on 21.03.16.
 */
public class LevyFlight extends Algorithm{
    //        alpha = 1; // cauchy
    float alpha = 1.5f; // levy
    //        alpha = 2; // gaussian
    float c = 1f;

    public LevyFlight() {
        super("Levy flight");
    }

    // c - scale(width of distribution)
    // alpha - exponent (controls the shape and tail)
    // alpha = 1.5 for an intermediate Levy distribution and flight
    public float Generate(float c, float alpha){
        float u,v;
        u = MathUtils.PI *(MathUtils.random() - 0.5f);

        // When alpha = 1, the distribution simplifies to Cauchy
        if(alpha == 1)
            return c* (float)Math.tan(u);
        v = 0;

        while (v == 0)
            v = -MathUtils.log(MathUtils.random(),10);

        // When alpha = 2, the distribution defaults to Gaussian
        if(alpha == 2)
            return 2 * c * (float)(Math.sqrt(v) * Math.sin(u));

        return (float)((c*Math.sin(alpha*u))/Math.pow(Math.cos(u),1/alpha)*Math.pow(Math.cos(u*(1-alpha))/v,(1-alpha)/alpha));
    }

    public Point generatePoint(Point lastPoint, int range) {
        float x0, x1, y0, y1;

        x0 = lastPoint.getX();
        x1 = Generate(c, alpha);
        x1 %= range / 2;
        if (x0 + x1 < 0 || x0 + x1 >= range) {
            x0 -= x1;
        } else {
            x0 += x1;
        }

        y0 = lastPoint.getY();
        y1 = Generate(c, alpha);
        y1 %= range / 2;
        if (y0 + y1 < 0 || y0 + y1 >= range) {
            y0 -= y1;
        } else {
            y0 += y1;
        }

        return new Point(x0, y0);
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }
}
