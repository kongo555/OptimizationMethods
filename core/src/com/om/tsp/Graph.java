package com.om.tsp;

import com.om.minimum.utils.Point;

import java.util.ArrayList;

/**
 * Created by kongo on 10.05.16.
 */
public class Graph {
    private String name;
    private ArrayList<Point> nodes;

    public Graph(String name, ArrayList<Point> nodes) {
        this.name = name;
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Point> getNodes() {
        return nodes;
    }
}
