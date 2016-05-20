package com.om.tsp.algorithm.antColony;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.utils.Point;
import com.om.tsp.TSP;
import com.om.tsp.Utils;
import com.om.tsp.algorithm.Algorithm;

import java.util.ArrayList;

/**
 * Created by kongo on 24.03.16.
 */
public class AntAlgorithm extends Algorithm{
    private ArrayList<Point> nodes;

    private int populationSize = 100;
    private ArrayList<Ant> ants;
    private double tau[][] = null; // pheromone
    private double distances[][] = null;

    public int[] bestTour;
    public double bestTourLength;

    // greedy
    public static double alpha = 0.1d;
    // rapid selection
    public static double beta = 9d;

    // heuristic parameters
    public static double Q = 0.0001;
    public static double PHEROMONE_PERSISTENCE = 0.8d;

    public AntAlgorithm(TSP tsp){
        super("Ant colony optimization", tsp);
    }

    public void init(ArrayList<Point> nodes) {
        this.nodes = nodes;
        distances = new double[nodes.size()][nodes.size()];
        int k = 0;
        for (Point r : nodes) {
            int j = 0;
            for (Point h : nodes) {
                distances[k][j] = Utils.calculateDistance(r.getX(), r.getY(), h.getX(),
                        h.getY());
                j++;
            }
            k++;
        }

        ants = new ArrayList<Ant>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            ants.add(new Ant(nodes.size()));
        }

        tau = new double[nodes.size()][nodes.size()];
        for (int i = 0; i < nodes.size(); i++)
            for (int j = 0; j < nodes.size(); j++)
                tau[i][j] = 1;

        bestTourLength = Integer.MAX_VALUE;
    }

    public void initAnts(){
        for (Ant ant : ants) {
            ant.clear();
            ant.visitNode(MathUtils.random(nodes.size()-1));
        }
    }

    public void moveAnts(){
        for (int i = 0; i < nodes.size() - 1; i++) {
            for (Ant ant : ants)
                ant.visitNode(getNextNode(ant));
        }
    }

    private int getNextNode(Ant ant) {
        int x = ant.getCurrentlyVisited();

        int secureNode = -1;
        final double[] probabilities = new double[nodes.size()];

        double denominator = 0d;
        for (int y = 0; y < nodes.size(); y++) {
            if (!ant.isVisited(y)) {
                double p = computeNumerator(x, y);
                probabilities[y] = p;
                denominator += p;
                secureNode = y;
            }
        }

        if (denominator == 0d) {
            return secureNode;
        }

        double sum = 0d;
        for (int y = 0; y < nodes.size(); y++) {
            double weighted = probabilities[y] / denominator;
            probabilities[y] = weighted;
            sum += weighted;
        }

        double random = MathUtils.random() * sum;
        for (int y = 0; y < nodes.size(); y++) {
            random -= probabilities[y];
            if (random <= 0) {
                return y;
            }
        }

        throw new RuntimeException("Not supposed to get here.");
    }


    private double computeNumerator(int x, int y){
        if (x != y) {
            double weight = tau[x][y];
            if (weight != 0d) {
                return (Math.pow(weight, alpha) * Math.pow(1d / distances[x][y], beta));
            }
        }
        return 0d;
    }

    private void updatePheromone() {
        for (int i = 0; i < nodes.size(); i++)
            for (int j = 0; j < nodes.size(); j++)
                tau[i][j] *= (1d - PHEROMONE_PERSISTENCE);

        for (Ant a : ants) {
            double contribution = Q / a.tourLength(distances);
            for (int i = 0; i < nodes.size() - 1; i++) {
                tau[a.getTour(i)][a.getTour(i + 1)] += contribution;
            }
            tau[a.getTour(nodes.size() - 1)][a.getTour(0)] += contribution;
        }
    }


    private void updateBest() {
        if (bestTour == null) {
            bestTour = ants.get(0).getTour();
            bestTourLength = ants.get(0).tourLength(distances);
        }
        for (Ant a : ants) {
            if (a.tourLength(distances) < bestTourLength) {
                bestTourLength = a.tourLength(distances);
                bestTour = a.getTour().clone();

                //System.out.println("Best tour length: " + (bestTourLength));
                //System.out.println("Best tour:" + tourToString(bestTour));
            }
        }
    }

    public void update() {
        initAnts();
        moveAnts();
        updatePheromone();
        updateBest();
    }

    public static String tourToString(int tour[]) {
        String t = new String();
        for (int i : tour)
            t = t + " " + i;
        return t;
    }

    public int[] getBestTour(){
        return bestTour;
    }

    public double getBestTourLength() {
        return bestTourLength;
    }

    @Override
    public VisTable buildParametersTable() {
        final VisTextField alphaTextField = new VisTextField("" + alpha);
        final VisTextField betaTextField = new VisTextField("" + beta);
        final VisTextField qTextField = new VisTextField("" + Q);
        final VisTextField phenomenaTextField = new VisTextField("" + PHEROMONE_PERSISTENCE);
        final VisTextField populationTextField = new VisTextField("" + populationSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                alpha = Double.parseDouble(alphaTextField.getText());
                beta = Double.parseDouble(betaTextField.getText());
                Q = Double.parseDouble(qTextField.getText());
                PHEROMONE_PERSISTENCE = Double.parseDouble(phenomenaTextField.getText());
                populationSize = Integer.parseInt(populationTextField.getText());

                init(nodes);
                tsp.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("alpha: "));
        parametersTable.add(alphaTextField);
        parametersTable.add(new VisLabel("beta: "));
        parametersTable.add(betaTextField).row();
        parametersTable.add(new VisLabel("Q: "));
        parametersTable.add(qTextField);
        parametersTable.add(new VisLabel("PP: "));
        parametersTable.add(phenomenaTextField).row();
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();

        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);


        return table;
    }
}
