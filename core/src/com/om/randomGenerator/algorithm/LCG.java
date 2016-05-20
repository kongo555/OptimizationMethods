package com.om.randomGenerator.algorithm;

import com.om.minimum.utils.Point;

/**
 * Created by kongo on 3/9/16.
 */
public class LCG extends Algorithm {
    private int a;
    private int c;
    private long m;
    private long seed;

    public LCG(long seed){
        super("Linear congruential generator");
        this.seed = seed;

//        a = 22695477;
//        c = 1;
//        m = (long) Math.pow(2,32);

        a = 111;
        c = 1;
        m = 1111111111;
    }

    public LCG(int a, int c, long m, long seed) {
        super("Linear congruential generator");
        this.a = a;
        this.c = c;
        this.m = m;
        this.seed = seed;
    }

    public int generate(){
        seed = (a * seed + c)% m;
        return (int)(seed % Integer.MAX_VALUE);
    }

    public int generate(int range){
        seed = (a * seed + c)% m;
        return (int)(seed % range);
    }

    public float generateFloat(){
        seed = (a * seed + c)% m;
        double u = seed;
        double d = m;
        double x = u/d;
        if(x < 0)
            x *= -1;
        return (float)x;
    }

    public Point generatePoint(int range){
        int x = generate(range);
        if(x < 0)
            x *= -1;
        int y = generate(range);
        if(y < 0)
            y *= -1;
        return new Point(x, y);
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public long getM() {
        return m;
    }

    public void setM(long m) {
        this.m = m;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }
}
