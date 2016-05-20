package com.om.minimum.algorithm;

import com.kotcrab.vis.ui.widget.VisTable;
import com.om.minimum.ConturPlot;
import com.om.minimum.Mapper;

/**
 * Created by kongo on 06.05.16.
 */
public abstract class Algorithm {
    protected String name;
    protected ConturPlot conturPlot;
    protected double x[];
    protected double y[];
    protected double xBestGlobal;
    protected double yBestGlobal;
    protected double zBestGlobal;
    public int numberOfIterations;
    public Mapper mapper;

    public Algorithm(String name, ConturPlot conturPlot) {
        this.name = name;
        this.conturPlot = conturPlot;
    }


    public abstract void init(Mapper mapper, int numberOfIterations);

    public abstract void update(int iterationCounter);

    public abstract VisTable buildParametersTable();

    public String getName() {
        return name;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public float getxBestGlobal() {
        return (float) xBestGlobal;
    }

    public float getyBestGlobal() {
        return (float) yBestGlobal;
    }

    public float getzBestGlobal() {
        return (float) zBestGlobal;
    }

}
