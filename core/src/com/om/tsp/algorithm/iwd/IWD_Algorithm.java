package com.om.tsp.algorithm.iwd;

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
 * Created by kongo on 25.03.16.
 */
public class IWD_Algorithm extends Algorithm {
    private ArrayList<Point> nodes;
    private double distances[][] = null;
    private double soil[][] = null;
    private ArrayList<Drop> drops;

    public int[] bestTour;
    public double bestTourLength;

    // Static parameters
    private int populationSize = 1000;

    private double av = 1;
    private double bv = 0.01;
    private double cv = 1;
    private double alpha = 1;

    private double as = 1;
    private double bs = 0.01;
    private double cs = 1;

    private double initSoil = 10000;
    private double initVelIWD = 200;
    private double initSoilIWD = 0;

    private double epsilon = 0.01;
    private double theta = 1;

    private double rhoN = 0.9;
    private double rhoIWD = 0.9;

    public IWD_Algorithm(TSP tsp){
        super("Intelligent water drops", tsp);
    }

    @Override
    public void init(ArrayList<Point> nodes) {
        this.nodes = nodes;

        distances = new double[nodes.size()][nodes.size()];
        int i = 0;
        for (Point r : nodes) {
            int j = 0;
            for (Point h : nodes) {
                distances[i][j] = Utils.calculateDistance(r.getX(), r.getY(), h.getX(),
                        h.getY());
                j++;
            }
            i++;
        }

        bestTourLength = Double.MAX_VALUE;
        drops = new ArrayList<Drop>(populationSize);
        for (int k = 0; k < populationSize; k++) {
            Drop drop = new Drop(0, 0, nodes.size());
            drops.add(drop);
        }

        soil = new double[nodes.size()][nodes.size()];
        for (int k = 0; k < nodes.size(); k++)
            for (int j = 0; j < nodes.size(); j++)
                soil[k][j] = initSoil;
    }

    private int getNextNode(Drop drop) {
        int x = drop.getCurrentlyVisited();
        final double[] probabilities = new double[nodes.size()];

        for (int y = 0; y < nodes.size(); y++) {
            if (!drop.isVisited(y)) {
                probabilities[y] = soil[x][y];
            }
        }

        double min = Double.MAX_VALUE;
        for (int y = 0; y < nodes.size(); y++) {
            if(probabilities[y] < min)
                min = probabilities[y];
        }

        if(min < 0){
            for (int y = 0; y < nodes.size(); y++)
                if(probabilities[y] != 0.0f)
                probabilities[y] -= min;
        }

        int secureNode = -1;
        double denominator = 0d;
        for (int y = 0; y < nodes.size(); y++) {
            if (!drop.isVisited(y)) {
                double p = computeNumerator(x, y, probabilities[y]);
                probabilities[y] = p;
                denominator += p;
                secureNode = y;
            }
        }

        if (denominator == 0d) {
            return secureNode;
        }

        //// FIXME
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


    private double computeNumerator(int x, int y, double g){
        if (x != y) {
            return  1 / (epsilon + g);
        }
        return 0d;
    }

    private void updateBest() {
        int[] iterationBestTour = null;
        double iterationBestTourLenght = Double.MAX_VALUE;
        double lenght;
        Drop bestDrop = null;
        for (int i = 1; i < drops.size(); i++) {
            Drop drop = drops.get(i);
            lenght = drop.tourLength(distances);
            if (lenght < iterationBestTourLenght) {
                iterationBestTour = drop.getTour();
                iterationBestTourLenght = drop.tourLength(distances);
                bestDrop = drop;
            }
        }

        for (int i = 0; i < nodes.size()-1; i++) {
            int x = iterationBestTour[i];
            int y = iterationBestTour[i + 1];

            // FIXME  soil(i, j)=(1 +ρIWD)∗soil(i, j)−ρIWD∗ 1 /q(Tiwb) - q(Tiwb)? moze usun bestdrop
            soil[x][y] = (1 + rhoIWD) * soil[x][y] - rhoIWD * 1 * bestDrop.getSoil() / (nodes.size() - 1);
        }


        if (iterationBestTourLenght < bestTourLength) {
            bestTour = iterationBestTour.clone();
            bestTourLength = iterationBestTourLenght;

            //System.out.println("Best tour length: " + (bestTourLength));
//            System.out.println("Best tour:" + tourToString(bestTour));
        }
    }

    public void update(){
       double velocity, h, tij, soilDelta;
       for (Drop drop : drops) {
           drop.reset(initVelIWD, initSoilIWD);
           drop.visitNode(MathUtils.random(nodes.size()-1));
       }

       for (int k = 0; k < nodes.size()-1; k++) {
           for (Drop drop : drops) {
               //move drop
               drop.visitNode(getNextNode(drop));

               int i = drop.getLastVisited();
               int j = drop.getCurrentlyVisited();
               //if(i == j)
                //   System.out.println("bug");

               //update drop velocity
               velocity = drop.getVelocity() + av / (bv + cv * Math.pow(soil[i][j], 2 * alpha));
               drop.setVelocity(velocity);

               //update trail soil
               h = distances[i][j] / 100;
               tij = h / drop.getVelocity();
               soilDelta = as / (bs + cs * Math.pow(tij, 2 * theta));
               soil[i][j] = (1 - rhoN) * soil[i][j] - rhoN * soilDelta;

               //update drop soil
               drop.setSoil(drop.getSoil() + soilDelta);
           }
       }
            updateBest();
      // new Scanner(System.in).next();
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
        final VisTextField avTextField = new VisTextField("" + av);
        final VisTextField asTextField = new VisTextField("" + as);
        final VisTextField bvTextField = new VisTextField("" + bv);
        final VisTextField bsTextField = new VisTextField("" + bs);
        final VisTextField cvTextField = new VisTextField("" + cv);
        final VisTextField csTextField = new VisTextField("" + cs);
        final VisTextField alphaTextField = new VisTextField("" + alpha);
        final VisTextField thetaTextField = new VisTextField("" + theta);
        final VisTextField rhoNField = new VisTextField("" + rhoN);
        final VisTextField rhoIWDField = new VisTextField("" + rhoIWD);
        final VisTextField initVelIWDField = new VisTextField("" + initVelIWD);
        final VisTextField initSoilIWDField = new VisTextField("" + initSoilIWD);
        final VisTextField initSoilField = new VisTextField("" + initSoil);
        final VisTextField populationTextField = new VisTextField("" + populationSize);

        //TODO parameters!!

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                av = Double.parseDouble(avTextField.getText());
                as = Double.parseDouble(asTextField.getText());
                bv = Double.parseDouble(bvTextField.getText());
                bs = Double.parseDouble(bsTextField.getText());
                cv = Double.parseDouble(cvTextField.getText());
                cs = Double.parseDouble(csTextField.getText());
                alpha = Double.parseDouble(alphaTextField.getText());
                theta = Double.parseDouble(thetaTextField.getText());
                rhoN = Double.parseDouble(rhoNField.getText());
                rhoIWD = Double.parseDouble(rhoIWDField.getText());
                initVelIWD = Double.parseDouble(initVelIWDField.getText());
                initSoilIWD = Double.parseDouble(initSoilIWDField.getText());
                initSoil = Double.parseDouble(initSoilField.getText());
                populationSize = Integer.parseInt(populationTextField.getText());

                init(nodes);
                tsp.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("av: "));
        parametersTable.add(avTextField);
        parametersTable.add(new VisLabel("as: "));
        parametersTable.add(asTextField).row();
        parametersTable.add(new VisLabel("bv: "));
        parametersTable.add(bvTextField);
        parametersTable.add(new VisLabel("bs: "));
        parametersTable.add(bsTextField).row();
        parametersTable.add(new VisLabel("cv: "));
        parametersTable.add(cvTextField);
        parametersTable.add(new VisLabel("cs: "));
        parametersTable.add(csTextField).row();
        parametersTable.add(new VisLabel("alpha: "));
        parametersTable.add(alphaTextField);
        parametersTable.add(new VisLabel("theta: "));
        parametersTable.add(thetaTextField).row();
        parametersTable.add(new VisLabel("rhoN: "));
        parametersTable.add(rhoNField);
        parametersTable.add(new VisLabel("rhoIWD: "));
        parametersTable.add(rhoIWDField).row();
        parametersTable.add(new VisLabel("initVelIWD: "));
        parametersTable.add(initVelIWDField);
        parametersTable.add(new VisLabel("initSoilIWD: "));
        parametersTable.add(initSoilIWDField).row();
        parametersTable.add(new VisLabel("initSoil: "));
        parametersTable.add(initSoilField);
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();

        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);


        return table;
    }
}
