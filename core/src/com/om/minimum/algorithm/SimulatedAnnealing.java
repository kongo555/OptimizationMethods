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
 * Created by kongo on 14.03.16.
 */
public class SimulatedAnnealing extends Algorithm {
    private double minTemp = 0.01f;
    private double startTemp = 100;
    private double temp;
    private double coolingRate = 0.01;

    private double x;
    private double xNew;
    private double y;
    private double yNew;
    private double z;
    private double zNew;

    public SimulatedAnnealing(ConturPlot conturPlot) {
        super("Simulated Annealing", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;

        super.x = new double[numberOfIterations];
        super.y = new double[numberOfIterations];

        x = mapper.randomX();
        y = mapper.randomY();
        zBestGlobal = mapper.f(x, y);

        temp = startTemp;
    }

    public void update(int iterationCounter) {
        // Loop until system has cooled
        if (temp > minTemp) {
            z = mapper.f(x, y);

//            xNew = mapper.randomX();
//            yNew = mapper.randomY();
            xNew = x + MathUtils.randomTriangular();
            yNew = y + MathUtils.randomTriangular();
            if (xNew < mapper.getxMin() || xNew > mapper.getxMax())
                xNew = xBestGlobal;
            if (yNew < mapper.getyMin() || yNew > mapper.getyMax())
                yNew = yBestGlobal;

            zNew = mapper.f(xNew, yNew);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(z, zNew, temp) > Math.random()) {
                x = xNew;
                y = yNew;
            }

            // Keep track of the best solution found
            if (z < zBestGlobal) {
                zBestGlobal = z;
                xBestGlobal = x;
                yBestGlobal = y;
            }

            // Cool system
            temp -= coolingRate;
        } else {
            z = zBestGlobal;
        }

        bindVariebles(iterationCounter);
    }

    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    public void bindVariebles(int iterationCounter) {
        super.x[iterationCounter] = x;
        super.y[iterationCounter] = y;
//        super.x[0] = x;
//        super.y[0] = y;
    }

    @Override
    public VisTable buildParametersTable() {
        final VisTextField tempTextField = new VisTextField("" + startTemp);
        final VisTextField coolingRateTextField = new VisTextField("" + coolingRate);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                startTemp = Double.parseDouble(tempTextField.getText());
                coolingRate = Double.parseDouble(coolingRateTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("Temp: "));
        parametersTable.add(tempTextField);
        parametersTable.add(new VisLabel("CoolingRate: "));
        parametersTable.add(coolingRateTextField).row();

        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);


        return table;
    }
}
