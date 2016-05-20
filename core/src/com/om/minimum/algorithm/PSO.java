package com.om.minimum.algorithm;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.ConturPlot;
import com.om.minimum.Mapper;

/**
 * Created by kongo on 21.03.16.
 */
public class PSO extends Algorithm {
    private Mapper mapper;

    private int populationSize = 20;
    private double vx[];
    private double vy[];

    private double xBest[];
    private double yBest[];

    private double vMax = 1;
    private double omega = 0.9;
    //private float c1 = 2; //0.9f;
    //private float c2 = 2; //0.9f;
    private double c1 = 0.9;
    private double c2 = 0.9;

    public PSO(ConturPlot conturPlot) {
        super("Particle swarm optimization", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;
        x = new double[populationSize];
        y = new double[populationSize];

        vx = new double[populationSize];
        vy = new double[populationSize];

        xBest = new double[populationSize];
        yBest = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            xBest[i] = x[i] = mapper.randomX();
            yBest[i] = y[i] = mapper.randomY();

            vx[i] = MathUtils.randomTriangular();
            vy[i] = MathUtils.randomTriangular();
        }

        xBestGlobal = x[0];
        yBestGlobal = y[0];
        zBestGlobal = mapper.f(xBestGlobal, yBestGlobal);

        for (int i = 1; i < populationSize; i++) {
            if (mapper.f(x[i], y[i]) < zBestGlobal) {//minimum
                xBestGlobal = x[i];
                yBestGlobal = y[i];
                zBestGlobal = mapper.f(x[i], y[i]);
            }
        }

    }

    public void update(int iterationCounter) {
        double rp, rg, tmp;

        for (int i = 0; i < populationSize; i++) {
            rp = MathUtils.random();
            rg = MathUtils.random();
            vx[i] = omega * vx[i] + c1 * rp * (xBest[i] - x[i]) + c2 * rg * (xBestGlobal - x[i]);
            if (vx[i] > vMax)
                vx[i] = vMax;
            else if (vx[i] < -vMax)
                vx[i] = -vMax;
            vy[i] = omega * vy[i] + c1 * rp * (yBest[i] - y[i]) + c2 * rg * (yBestGlobal - y[i]);
            if (vy[i] > vMax)
                vy[i] = vMax;
            else if (vy[i] < -vMax)
                vy[i] = -vMax;

            x[i] += vx[i];
            y[i] += vy[i];

            tmp = mapper.f(x[i], y[i]);
            if (tmp < mapper.f(xBest[i], yBest[i])) {
                xBest[i] = x[i];
                yBest[i] = y[i];
                if (tmp < zBestGlobal) {
                    xBestGlobal = x[i];
                    yBestGlobal = y[i];
                    zBestGlobal = tmp;
                }
            }
        }
    }

    public VisTable buildParametersTable() {
        final VisTextField omegaTextField = new VisTextField("" + omega);
        final VisTextField c1TextField = new VisTextField("" + c1);
        final VisTextField c2lphaTextField = new VisTextField("" + c2);
        final VisTextField vMaxTextField = new VisTextField("" + vMax);
        final VisTextField populationTextField = new VisTextField("" + populationSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                omega = Double.parseDouble(omegaTextField.getText());
                c1 = Double.parseDouble(c1TextField.getText());
                c2 = Double.parseDouble(c2lphaTextField.getText());
                vMax = Double.parseDouble(vMaxTextField.getText());
                populationSize = Integer.parseInt(populationTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("omega: "));
        parametersTable.add(omegaTextField);
        parametersTable.add(new VisLabel("c1: "));
        parametersTable.add(c1TextField).row();
        parametersTable.add(new VisLabel("c2: "));
        parametersTable.add(c2lphaTextField);
        parametersTable.add(new VisLabel("vMax: "));
        parametersTable.add(vMaxTextField).row();
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();
        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
