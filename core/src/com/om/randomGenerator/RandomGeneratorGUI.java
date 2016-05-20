package com.om.randomGenerator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import com.om.ProblemGUI;

/**
 * Created by kongo on 08.05.16.
 */
public class RandomGeneratorGUI extends ProblemGUI {
    private RandomGenerator randomGenerator;
    private VisTable table;
    private VisLabel iterationsLabel;
    private VisTable parametersTable;

    public RandomGeneratorGUI(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;

        iterationsLabel = new VisLabel("Iterations: " + 0);

        table = new VisTable(true);
        table.top();
        table.add(new VisLabel(randomGenerator.getName())).align(Align.left).row();
        table.add(buildMenuBar().getTable()).padBottom(30).row();
        table.add(new VisLabel("Info:")).row();
        table.add(iterationsLabel).row();
        table.add(buildSpeedTable()).padBottom(pad).row();
    }

    public void update(int iterations) {
        setIterationsLabel(iterations);
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(buildAlgorithmMenu());

        return menuBar;
    }

    private Menu buildAlgorithmMenu() {
        final Menu functionMenu = new Menu("Choose Algorithm");
        for (final RandomGeneratorRenderer randomGeneratorRenderer : randomGenerator.getRandomGeneratorRenderers()) {
            functionMenu.addItem(new MenuItem(randomGeneratorRenderer.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    randomGenerator.setRandomGeneratorRenderer(randomGeneratorRenderer);
                }
            }));
        }

        return functionMenu;
    }

    private void setIterationsLabel(int iterations) {
        iterationsLabel.setText("Iterations: " + iterations);
    }

    private VisSlider buildSpeedTable() {
        final VisSlider speedSlider = new VisSlider(1, 100, 1, false);
        //final VisLabel speedLabel = new VisLabel("");
        //speedLabel.setText("Speed: " + speedSlider.getValue());
        speedSlider.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                randomGenerator.setSpeed((int)speedSlider.getValue());
                //speedLabel.setText("Speed: " + speedSlider.getValue());
            }
        });
        speedSlider.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                randomGenerator.setSpeed((int)speedSlider.getValue());
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
        table.removeActor(this.parametersTable);
        this.parametersTable = parametersTable;
        table.add(parametersTable);
    }

    @Override
    public Table getTable() {
        return table;
    }
}
