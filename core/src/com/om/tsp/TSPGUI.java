package com.om.tsp;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import com.om.ProblemGUI;
import com.om.tsp.algorithm.Algorithm;

/**
 * Created by kongo on 09.05.16.
 */
public class TSPGUI extends ProblemGUI {
    private TSP tsp;
    private VisTable table;

    private VisLabel iterationsLabel;
    private VisLabel distanceLabel;
    private VisTable parametersTable;

    public TSPGUI(TSP tsp) {
        this.tsp = tsp;

        distanceLabel = new VisLabel("Distance: " + 0);
        iterationsLabel = new VisLabel("Iterations: " + 0);


        table = new VisTable(true);
        table.top();
        table.add(new VisLabel(tsp.getName())).align(Align.left).row();
        table.add(buildMenuBar().getTable()).padBottom(30).row();

        table.add(new VisLabel("Info:")).row();
        table.add(distanceLabel).row();
        table.add(iterationsLabel).row();
        table.add(buildSpeedTable()).padBottom(pad).row();

    }

    public void update(double distance, int iterations) {
        setDistanceLabel(distance);
        setIterationsLabel(iterations);
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(buildAlgorithmMenu());
        menuBar.addMenu(buildGraphMenu());

        return menuBar;
    }

    private Menu buildAlgorithmMenu() {
        final Menu functionMenu = new Menu("Choose Algorithm");
        for (final Algorithm algorithm : tsp.getAlgorithms()) {
            functionMenu.addItem(new MenuItem(algorithm.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    tsp.setAlgorithm(algorithm);
                }
            }));
        }

        return functionMenu;
    }

    private Menu buildGraphMenu() {
        final Menu functionMenu = new Menu("Choose Graph");
        for (final Graph graph : tsp.getGraphs()) {
            functionMenu.addItem(new MenuItem(graph.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    tsp.setGraph(graph);
                }
            }));
        }

        return functionMenu;
    }

    private void setIterationsLabel(int iterations) {
        iterationsLabel.setText("Iterations: " + iterations);
    }

    private void setDistanceLabel(double distance) {
        String string = String.format("Distance: %.2f", distance);
        distanceLabel.setText(string);
    }

    private VisSlider buildSpeedTable() {
        final VisSlider speedSlider = new VisSlider(1, 100, 1, false);
        //final VisLabel speedLabel = new VisLabel("");
        //speedLabel.setText("Speed: " + speedSlider.getValue());
        speedSlider.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                tsp.setSpeed(1 / speedSlider.getValue());
                //speedLabel.setText("Speed: " + speedSlider.getValue());
            }
        });
        speedSlider.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tsp.setSpeed(1 / speedSlider.getValue());
                //speedLabel.setText("Speed: " + speedSlider.getValue());
                return false;
            }
        });

        VisTable table = new VisTable(true);
        //table.add(speedLabel);
        table.add(speedSlider);

        return speedSlider;
    }

    public void setParametersTable(VisTable parametersTable) {
        if(this.parametersTable != null)
            table.removeActor(this.parametersTable);
        this.parametersTable = parametersTable;
        table.add(parametersTable);
    }

    @Override
    public Table getTable() {
        return table;
    }
}
