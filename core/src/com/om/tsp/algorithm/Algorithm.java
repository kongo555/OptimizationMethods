package com.om.tsp.algorithm;

import com.kotcrab.vis.ui.widget.VisTable;
import com.om.minimum.utils.Point;
import com.om.tsp.TSP;

import java.util.ArrayList;

/**
 * Created by kongo on 09.05.16.
 */
public abstract class Algorithm {
    protected String name;
    protected TSP tsp;

    public Algorithm(String name, TSP tsp){
        this.name = name;
        this.tsp = tsp;
    }

    public abstract void init(ArrayList<Point> nodes);

    public abstract void update();

    public abstract int[] getBestTour();

    public abstract double getBestTourLength();

    public String getName() {
        return name;
    }

    public abstract VisTable buildParametersTable();
}
