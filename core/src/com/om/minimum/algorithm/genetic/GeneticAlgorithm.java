package com.om.minimum.algorithm.genetic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.ConturPlot;
import com.om.minimum.algorithm.Algorithm;
import com.om.minimum.Mapper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kongo on 27.03.16.
 */
public class GeneticAlgorithm extends Algorithm {
    private int populationSize = 200;
    private ArrayList<Chromosome> population;
    private ArrayList<Chromosome> newPopulation;

    private Chromosome best;
    int keep;
    double[] p;
    private double mutationRate = 0.2;

    public GeneticAlgorithm(ConturPlot conturPlot) {
        super("Classic Genetic Algorithm", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;
        x = new double[populationSize];
        y = new double[populationSize];

        population = new ArrayList<Chromosome>(populationSize);
        newPopulation = new ArrayList<Chromosome>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = new Chromosome(mapper.randomX(), mapper.randomY());
            chromosome.updateZ(mapper);
            population.add(chromosome);
        }
        Collections.sort(population);

        best = population.get(0);
        keep = (int) Math.floor(0.5 * populationSize);
        p = new double[keep];
        calculateProbability();
    }

    // probability that the chromosome in n'th place will be a parent
    public void calculateProbability() {
        double sum = 0;
        for (int i = 1; i <= keep; i++)
            sum += i;

        for (int i = 1; i <= keep; i++)
            p[i - 1] = (keep - i + 1) / sum;
    }

    public void update(int iterationCounter) {
        Chromosome parentA;
        Chromosome parentB;

        for (int i = 0; i < keep; i++) {
            parentA = getParent();
            parentB = getParent();

            if (MathUtils.random() > 0.5)
                crossover(parentA, parentB, true);
            else
                crossover(parentA, parentB, false);
        }

        population.clear();
        population.addAll(newPopulation);
        newPopulation.clear();

        mutation();

        for (int i = 0; i < populationSize; i++)
            population.get(i).updateZ(mapper);
        Collections.sort(population);
        if (population.get(0).getZ() < best.getZ())
            best = population.get(0);

        bindVariebles();
    }

    public Chromosome getParent() {
        Chromosome parent = null;
        double sum = 0;
        double random = MathUtils.random();
        for (int i = 0; i < keep; i++) {
            sum += p[i];
            if (random < sum) {
                parent = population.get(i);
                break;
            }
        }
        if (parent == null)
            System.out.println("null!!!!");

        return parent;
    }

    public void crossover(Chromosome parentA, Chromosome parentB, boolean first) {
        double beta = MathUtils.random();
        if (first) {
            double xNew1 = (1 - beta) * parentA.getX() + beta * parentB.getX();
            double xNew2 = (1 - beta) * parentB.getX() + beta * parentA.getX();
            newPopulation.add(new Chromosome(xNew1, parentB.getY()));
            newPopulation.add(new Chromosome(xNew2, parentA.getY()));
        } else {
            double yNew1 = (1 - beta) * parentA.getY() + beta * parentB.getY();
            double yNew2 = (1 - beta) * parentB.getY() + beta * parentA.getY();
            newPopulation.add(new Chromosome(parentB.getX(), yNew1));
            newPopulation.add(new Chromosome(parentA.getX(), yNew2));
        }
    }

    public void mutation() {
        for (int i = 0; i < populationSize; i++) {
            if (MathUtils.random() < mutationRate) {
                population.get(i).setX(population.get(i).getX() + MathUtils.randomTriangular() * 2);
                population.get(i).setY(population.get(i).getY() + MathUtils.randomTriangular() * 2);
            }
        }
    }

    public void bindVariebles() {
        xBestGlobal = best.getX();
        yBestGlobal = best.getY();
        zBestGlobal = best.getZ();

        for (int i = 0; i < population.size(); i++) {
            x[i] = population.get(i).getX();
            y[i] = population.get(i).getY();
        }
    }

    @Override
    public VisTable buildParametersTable() {
        final VisTextField mutationRateTextField = new VisTextField("" + mutationRate);
        final VisTextField populationTextField = new VisTextField("" + populationSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                mutationRate = Double.parseDouble(mutationRateTextField.getText());
                populationSize = Integer.parseInt(populationTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("MutationRate: "));
        parametersTable.add(mutationRateTextField);
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();

        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);


        return table;
    }

}
