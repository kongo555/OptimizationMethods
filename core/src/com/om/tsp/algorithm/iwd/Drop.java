package com.om.tsp.algorithm.iwd;

/**
 * Created by kongo on 25.03.16.
 */
public class Drop {
    private double velocity;
    private double soil;

    private boolean visited[];
    private int tour[];
    private int tourCounter;

    public Drop(double velocity, double soil, int nodesSize){
        this.velocity = velocity;
        this.soil = soil;
        visited = new boolean[nodesSize];
        tour = new int[nodesSize];
        tourCounter = 0;
    }

    public void reset(double velocity, double soil){
        this.velocity = velocity;
        this.soil = soil;
        tourCounter = 0;
        for (int i = 0; i < visited.length; i++)
            visited[i] = false;
    }

    public double tourLength(double distance[][]) {
        double length = distance[tour[tour.length - 1]][tour[0]];
        for (int i = 0; i < tourCounter - 1; i++) {
            length += distance[tour[i]][tour[i + 1]];
        }
        return length;
    }

    public double getSoil() {
        return soil;
    }

    public void setSoil(double soil) {
        this.soil = soil;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public boolean isVisited(int i){
        return visited[i];
    }

    public void visitNode(int i){
        visited[i] = true;
        tour[tourCounter] = i;
        tourCounter++;
    }

    public int getCurrentlyVisited(){
        return tour[tourCounter-1];
    }

    public int getLastVisited(){
        return tour[tourCounter-2];
    }

    public int getTour(int i){
        return tour[i];
    }

    public int[] getTour() {
        return tour;
    }

    public int getTourCounter() {
        return tourCounter;
    }
}
