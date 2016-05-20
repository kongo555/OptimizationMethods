package com.om.tsp.algorithm.antColony;

/**
 * Created by kongo on 24.03.16.
 */
public class Ant {
    private boolean visited[];
    private int tour[];
    private int tourCounter;

    public Ant(int nodesSize){
        visited = new boolean[nodesSize];
        tour = new int[nodesSize];
        tourCounter = 0;
    }

    public double tourLength(double distance[][]) {
        double length = distance[tour[tour.length - 1]][tour[0]];
        for (int i = 0; i < tourCounter - 1; i++) {
            length += distance[tour[i]][tour[i + 1]];
        }
        return length;
    }

    public void clear(){
        tourCounter = 0;
        for (int i = 0; i < visited.length; i++)
            visited[i] = false;
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
