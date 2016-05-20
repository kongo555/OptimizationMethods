package com.om.minimum;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import com.om.ProblemGUI;
import com.om.minimum.algorithm.Algorithm;
import com.om.minimum.utils.Function;

/**
 * Created by kongo on 06.05.16.
 */
public class ConturPlotGUI extends ProblemGUI {
    private ConturPlot conturPlot;
    private VisTable table;

    private VisLabel minimumLabel;
    private VisLabel iterationsLabel;
    private VisTable parametersTable;

    private VisTextField functionTextField;
    private VisTextField xMinTextField;
    private VisTextField xMaxTextField;
    private VisTextField yMinTextField;
    private VisTextField yMaxTextField;

    public ConturPlotGUI(ConturPlot conturPlot) {
        this.conturPlot = conturPlot;

        minimumLabel = new VisLabel(String.format("Current minimum: x = %.2f    y = %.2f    z = %.5f", 0.0f, 0.0f, 0.0f));
        iterationsLabel = new VisLabel("Iterations: " + 0);

        parametersTable = conturPlot.getAlgorithm().buildParametersTable();

        table = new VisTable(true);
        table.top();
        table.add(new VisLabel(conturPlot.getName())).align(Align.left).row();
        table.add(buildMenuBar().getTable()).padBottom(30).row();

        table.add(buildFunctionTable(conturPlot.getFunctions().get(0))).padBottom(pad).row();

        table.add(new VisLabel("Info:")).row();
        table.add(minimumLabel).row();
        table.add(iterationsLabel).row();
        table.add(buildSpeedTable()).padBottom(pad).row();

        table.add(parametersTable);
    }

    public void update(float x, float y, float z, int iterations) {
        setMinimumLabel(x, y, z);
        setIterationsLabel(iterations);
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(buildAlgorithmMenu());
        menuBar.addMenu(buildFunctionMenu());

        return menuBar;
    }

    private Menu buildAlgorithmMenu() {
        final Menu functionMenu = new Menu("Choose Algorithm");
        for (final Algorithm algorithm : conturPlot.getAlgorithms()) {
            functionMenu.addItem(new MenuItem(algorithm.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    conturPlot.setAlgorithm(algorithm);
                }
            }));
        }

        return functionMenu;
    }

    private Menu buildFunctionMenu() {
        final Menu functionMenu = new Menu("Choose Function");
        for (final Function function : conturPlot.getFunctions()) {
            functionMenu.addItem(new MenuItem(function.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    setFunction(function);
                }
            }));
        }

        return functionMenu;
    }

    private void setFunction(Function function) {
        functionTextField.setText(function.getText());
        xMinTextField.setText("" + function.getxMin());
        xMaxTextField.setText("" + function.getxMax());
        yMinTextField.setText("" + function.getyMin());
        yMaxTextField.setText("" + function.getyMax());
        conturPlot.setFunction(function);
    }

    private VisTable buildFunctionTable(Function function) {
        functionTextField = new VisTextField(function.getText());
        xMinTextField = new VisTextField("" + function.getxMin());
        xMaxTextField = new VisTextField("" + function.getxMax());
        yMinTextField = new VisTextField("" + function.getyMin());
        yMaxTextField = new VisTextField("" + function.getyMax());
        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                conturPlot.setFunction(new Function("Custom", functionTextField.getText(),
                        Float.parseFloat(xMinTextField.getText()), Float.parseFloat(xMaxTextField.getText()),
                        Float.parseFloat(yMinTextField.getText()), Float.parseFloat(yMaxTextField.getText())));
            }
        });
        VisTable table = new VisTable(true);
        VisTable functionTable = new VisTable(true);
        functionTable.add(new VisLabel("Function: "));
        functionTable.add(functionTextField).prefWidth(400);
        VisTable minMaxTable = new VisTable(true);
        minMaxTable.add(new VisLabel("x min: "));
        minMaxTable.add(xMinTextField);
        minMaxTable.add(new VisLabel("x max: "));
        minMaxTable.add(xMaxTextField).row();
        minMaxTable.add(new VisLabel("y min: "));
        minMaxTable.add(yMinTextField);
        minMaxTable.add(new VisLabel("y max: "));
        minMaxTable.add(yMaxTextField);
        table.add(new VisLabel("Function settings:")).row();
        table.add(functionTable).row();
        table.add(minMaxTable).row();
        table.add(button);

        return table;
    }

    private void setMinimumLabel(float x, float y, float z) {
        String string = String.format("Current minimum: x = %.2f    y = %.2f    z = %.5f", x, y, z);
        minimumLabel.setText(string);
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
                conturPlot.setSpeed(1 / speedSlider.getValue());
                //speedLabel.setText("Speed: " + speedSlider.getValue());
            }
        });
        speedSlider.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                conturPlot.setSpeed(1 / speedSlider.getValue());
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
