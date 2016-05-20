package com.om.tsp;

import com.om.minimum.utils.Point;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kongo on 25.03.16.
 */
public class Utils {
    public static final double calculateDistance(double x1, double y1, double x2, double y2) {
        final double x = x2 - x1;
        final double y = y2 - y1;
        return Math.abs((Math.sqrt((x * x) + (y * y))));
    }

    public static Graph readFile(String path) throws IOException {
        FileReader fr = null;
        try {
            fr = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buf = new BufferedReader(fr);
        String line;
        String name = null;
        ArrayList<Point> nodes = new ArrayList<Point>(60);
        float x,y;
        while ((line = buf.readLine()) != null) {
            String split[] = line.split(" ");

            if(split[0].equals( "NAME:"))
                name = split[1];
            else if(split[0].equals( "SIZE:"))
                nodes = new ArrayList<Point>(Integer.parseInt(split[1]));
            else {
                x = Float.parseFloat(split[1]);
                y = Float.parseFloat(split[2]);
                nodes.add(new Point(x, y));
            }
            if (line.equals("EOF")) {
                break;
            }
        }

        return new Graph(name, nodes);
    }

}
