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
 * Created by kongo on 22.03.16.
 */
public class BatAlgorithm extends Algorithm {
    private int population = 40;
    private double z[];
    private double vx[];
    private double vy[];
    private double f[];
    private double Ai[];
    private double ri[];

    private double A = 0.9;
    private double r = 0.9;

    private double xNew;
    private double yNew;
    private double zNew;

    private double fMin = 0;
    private double fMax = 1;

    private double Amin = 0;

    private double alpha;
    private double gama = alpha = 0.9;


    public BatAlgorithm(ConturPlot conturPlot) {
        super("Bat Algorithm", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;
        x = new double[population];
        y = new double[population];
        z = new double[population];
        vx = new double[population];
        vy = new double[population];
        f = new double[population];

        Ai = new double[population];
        ri = new double[population];

        for (int i = 0; i < population; i++) {
            x[i] = mapper.randomX();
            y[i] = mapper.randomY();
            z[i] = mapper.f(x[i], y[i]);
            vx[i] = 0;
            vy[i] = 0;
            f[i] = 0;

            Ai[i] = MathUtils.random() + 1;
            ri[i] = MathUtils.random() + Float.MIN_NORMAL;

            if (i == 0 || z[i] < zBestGlobal) {
                xBestGlobal = x[i];
                yBestGlobal = y[i];
                zBestGlobal = z[i];
            }
        }
    }

    public void update(int iterationCounter) {
        float averageA = 0;
        for (int i = 0; i < population; i++) {
            averageA += Ai[i];
        }
        averageA /= population;

        for (int i = 0; i < population; i++) {
            f[i] = MathUtils.random() * (fMax - fMin) + fMin;

            vx[i] = vx[i] + (x[i] - xBestGlobal) * f[i];
            vy[i] = vy[i] + (y[i] - yBestGlobal) * f[i];

            xNew = x[i] + vx[i];
            yNew = y[i] + vy[i];

            // pulse rate
            if (MathUtils.random() > r) {
                xNew = xBestGlobal + MathUtils.randomTriangular() * averageA;
                yNew = yBestGlobal + MathUtils.randomTriangular() * averageA;
            }

            zNew = mapper.f(xNew, yNew);
            // update if solution is improved, and not too loud
            if ((zNew <= z[i]) && (MathUtils.random() < A)) {
                x[i] = xNew;
                y[i] = yNew;
                z[i] = zNew;

                if (zNew < zBestGlobal) {
                    xBestGlobal = x[i];
                    yBestGlobal = y[i];
                    zBestGlobal = z[i];
                }

                Ai[i] *= alpha;
                if (Ai[i] < Amin)
                    Ai[i] = Amin;
                ri[i] *= (1 - Math.exp(-gama * iterationCounter));
                //System.out.println(Ai[i] + " " + ri[i]);
            }
            //System.out.println(i + " " + x[i] + " " + y[i] + " " + z[i]);
        }
//        System.out.println(zBest);
    }

    public VisTable buildParametersTable() {
        final VisTextField ATextField = new VisTextField("" + A);
        final VisTextField rTextField = new VisTextField("" + r);
        final VisTextField alphaTextField = new VisTextField("" + alpha);
        final VisTextField gamaTextField = new VisTextField("" + gama);
        final VisTextField populationTextField = new VisTextField("" + population);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                A = Double.parseDouble(ATextField.getText());
                r = Double.parseDouble(rTextField.getText());
                alpha = Double.parseDouble(alphaTextField.getText());
                gama = Double.parseDouble(gamaTextField.getText());
                population = Integer.parseInt(populationTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("A: "));
        parametersTable.add(ATextField);
        parametersTable.add(new VisLabel("r: "));
        parametersTable.add(rTextField).row();
        parametersTable.add(new VisLabel("alpha: "));
        parametersTable.add(alphaTextField);
        parametersTable.add(new VisLabel("gama: "));
        parametersTable.add(gamaTextField).row();
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();
        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
