package com.om.randomGenerator.algorithm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.utils.Point;
import com.om.randomGenerator.RandomGenerator;
import com.om.randomGenerator.RandomGeneratorRenderer;

import java.util.ArrayList;

/**
 * Created by kongo on 08.05.16.
 */
public class GaussianRenderer extends RandomGeneratorRenderer {
    private Gaussian gaussian;
    private ArrayList<Point> points;

    public GaussianRenderer(RandomGenerator randomGenerator, int width, int height) {
        super(randomGenerator, width, height);

        gaussian = new Gaussian(22695477, 1, (long) Math.pow(2,32));
    }

    @Override
    public void init() {
        points = new ArrayList<Point>(100000);
    }

    @Override
    public void update() {
        points.add(gaussian.generate(functionWidth/10));
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        renderBackground(shapeRenderer);
        renderPoints(shapeRenderer);
    }

    private void renderBackground(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x0, y0, functionWidth, functionHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x0, y0, functionWidth, functionHeight);
        shapeRenderer.end();
    }

    public void renderPoints(ShapeRenderer shapeRenderer) {
        float x, y;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        for (Point point : points) {
            x = x0 + (functionWidth / 2) + point.getX();
            y = y0 + (functionHeight / 2) + point.getY();
            shapeRenderer.rect(x, y, 5, 5);
        }
        shapeRenderer.end();
    }

    @Override
    public String getName() {
        return gaussian.getName();
    }

    public VisTable buildParametersTable() {
        final VisTextField aTextField = new VisTextField("" + gaussian.getLcg().getA());
        final VisTextField cTextField = new VisTextField("" + gaussian.getLcg().getC());
        final VisTextField mTextField = new VisTextField("" + gaussian.getLcg().getM());
        final VisTextField seedTextField = new VisTextField("" + gaussian.getLcg().getSeed());
        //final VisTextField cellSizeTextField = new VisTextField("" + cellSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                gaussian.getLcg().setA(Integer.parseInt(aTextField.getText()));
                gaussian.getLcg().setC(Integer.parseInt(cTextField.getText()));
                gaussian.getLcg().setM(Long.parseLong(mTextField.getText()));
                gaussian.getLcg().setSeed(Long.parseLong(seedTextField.getText()));
                //cellSize = Integer.getInteger(cellSizeTextField.getText());

                init();
                randomGenerator.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("a: "));
        parametersTable.add(aTextField);
        parametersTable.add(new VisLabel("c: "));
        parametersTable.add(cTextField).row();
        parametersTable.add(new VisLabel("m: "));
        parametersTable.add(mTextField);
        parametersTable.add(new VisLabel("seed: "));
        parametersTable.add(seedTextField).row();
        //parametersTable.add(new VisLabel("CellSize: "));
        //parametersTable.add(cellSizeTextField).row();
        table.add(new VisLabel(gaussian.getName() + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
