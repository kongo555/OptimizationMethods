package com.om.randomGenerator.algorithm;

import com.om.minimum.utils.Point;

/**
 * Created by kongo on 3/9/16.
 */
public class Gaussian extends Algorithm {
    private LCG lcg;

    public Gaussian(int a, int c, long m){
        super("Gaussian");
        lcg = new LCG(a, c, m, 1);
    }

    // max 3!
    public Point generate(float scale){
        float v1, v2, w;

        do {
            v1 = 2.0f * lcg.generateFloat() - 1.0f;
            v2 = 2.0f * lcg.generateFloat() - 1.0f;
            w = v1 * v1 + v2 * v2;
        } while ( w >= 1.0f );

        w = (float)Math.sqrt( (-2.0f * Math.log( w ) ) / w );
        v1 = v1 * w;
        v2 = v2 * w;

        return new Point(v1 * scale, v2 * scale);
    }

    public LCG getLcg() {
        return lcg;
    }
}
