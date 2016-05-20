package com.om.minimum.algorithm;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.ConturPlot;
import com.om.minimum.algorithm.genetic.Chromosome;
import com.om.minimum.Mapper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kongo on 29.03.16.
 */
public class EvolutionStrategy extends Algorithm {
    private int populationSize = 40;
    private ArrayList<Chromosome> population;
    private ArrayList<Chromosome> newPopulation;
    private Chromosome best;

    double C = 0.5;
    double F = 1;

    public EvolutionStrategy(ConturPlot conturPlot) {
        super("Evolution strategy", conturPlot);
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
    }

    public void update(int iterationCounter) {
        for (int i = 0; i < populationSize; i++) {
            Chromosome chromosome = population.get(i);
            Chromosome v = mutate();
            Chromosome u = recombine(chromosome, v);
            newPopulation.add(Selection(chromosome, u));
        }

        population.clear();
        population.addAll(newPopulation);
        newPopulation.clear();

        for (Chromosome chromosome : population) {
            chromosome.updateZ(mapper);
            if (chromosome.getZ() < best.getZ())
                best = chromosome;
        }

        bindVariebles();
    }

    private Chromosome mutate() {
        Chromosome p1 = population.get(MathUtils.random(populationSize - 1));
        Chromosome p2 = population.get(MathUtils.random(populationSize - 1));
        Chromosome p3 = population.get(MathUtils.random(populationSize - 1));
        double x = p1.getX() + F * (p2.getX() - p3.getX());
        double y = p1.getY() + F * (p2.getY() - p3.getY());
        Chromosome v = new Chromosome(x, y);
        v.updateZ(mapper);

        return v;

    }

    private Chromosome recombine(Chromosome chromosome, Chromosome v) {
        if (MathUtils.random() <= C)
            return v;
        else
            return chromosome;
    }

    private Chromosome Selection(Chromosome chromosome, Chromosome u) {
        u.updateZ(mapper);
        if (u.getZ() < chromosome.getZ())
            return u;
        else
            return chromosome;
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
        final VisTextField CTextField = new VisTextField("" + C);
        final VisTextField FTextField = new VisTextField("" + F);
        final VisTextField populationTextField = new VisTextField("" + populationSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                C = Double.parseDouble(CTextField.getText());
                F = Double.parseDouble(FTextField.getText());
                populationSize = Integer.parseInt(populationTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("C: "));
        parametersTable.add(CTextField);
        parametersTable.add(new VisLabel("F: "));
        parametersTable.add(FTextField).row();
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();

        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);


        return table;
    }
}
