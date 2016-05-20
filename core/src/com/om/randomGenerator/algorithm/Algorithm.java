package com.om.randomGenerator.algorithm;

import com.kotcrab.vis.ui.widget.VisTable;
import com.om.minimum.utils.Point;

/**
 * Created by kongo on 08.05.16.
 */
public abstract class Algorithm {
    private String name;

    public Algorithm(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
