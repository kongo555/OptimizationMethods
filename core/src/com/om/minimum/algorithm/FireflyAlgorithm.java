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
 * Created by kongo on 20.03.16.
 */
public class FireflyAlgorithm extends Algorithm {

    private int populationSize = 30;
    private double lightIntensity[];

    double alpha = 0.2;
    double beta0 = 1;
    double gama = 1.0;

    public FireflyAlgorithm(ConturPlot conturPlot) {
        super("Firefly Algorithm", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;

        x = new double[populationSize];
        y = new double[populationSize];
        lightIntensity = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            x[i] = mapper.randomX();
            y[i] = mapper.randomY();
            lightIntensity[i] = mapper.f(x[i], y[i]);
        }
        zBestGlobal = Double.MAX_VALUE;
    }

    public void update(int iterationCounter) {
        double r, beta;
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length; j++) {
                if (i == j)
                    continue;
                if (lightIntensity[j] < lightIntensity[i]) {
                    r = (x[j] - x[i]) * (x[j] - x[i]);
                    r += (y[j] - y[i]) * (y[j] - y[i]);
                    r = (float) Math.sqrt(r);

                    beta = beta0 * (float) Math.exp(-gama * (r * r));
                    x[i] = x[i] + beta * (x[j] - x[i]) + alpha * MathUtils.randomTriangular();
                    y[i] = y[i] + beta * (y[j] - y[i]) + alpha * MathUtils.randomTriangular();

                    lightIntensity[i] = mapper.f(x[i], y[i]);
                }
            }
            if (zBestGlobal > lightIntensity[i]) {
                xBestGlobal = x[i];
                yBestGlobal = y[i];
                zBestGlobal = lightIntensity[i];
            }
        }
    }

    public VisTable buildParametersTable() {
        final VisTextField alphaTextField = new VisTextField("" + alpha);
        final VisTextField beta0TextField = new VisTextField("" + beta0);
        final VisTextField gamaTextField = new VisTextField("" + gama);
        final VisTextField populationTextField = new VisTextField("" + populationSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                alpha = Double.parseDouble(alphaTextField.getText());
                beta0 = Double.parseDouble(beta0TextField.getText());
                gama = Double.parseDouble(gamaTextField.getText());
                populationSize = Integer.parseInt(populationTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("alpha: "));
        parametersTable.add(alphaTextField);
        parametersTable.add(new VisLabel("beta0: "));
        parametersTable.add(beta0TextField).row();
        parametersTable.add(new VisLabel("gama: "));
        parametersTable.add(gamaTextField);
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();
        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
