package com.om.randomGenerator.algorithm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
 * Created by kongo on 21.03.16.
 */
public class LevyFlightRenderer  extends RandomGeneratorRenderer {
    private LevyFlight levyFlight;
    private ArrayList<Point> points;

    public LevyFlightRenderer(RandomGenerator randomGenerator, int width, int height) {
        super(randomGenerator, width, height);

        levyFlight = new LevyFlight();
    }

    public void init(){
        points = new ArrayList<Point>(1000);
        points.add(new Point(MathUtils.random(functionWidth), MathUtils.random(functionWidth)));
    }

    @Override
    public void update() {
        points.add(levyFlight.generatePoint(points.get(points.size()-1), functionWidth));
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        renderBackground(shapeRenderer);
        renderPoints(shapeRenderer);
    }

    private void renderPoints(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        float previousX = x0 + points.get(0).getX();
        float previousY = y0 + points.get(0).getY();
        float currentX;
        float currentY;
        for (int i = 1; i < points.size(); i++) {
            currentX = x0 + points.get(i).getX();
            currentY = y0 + points.get(i).getY();
            shapeRenderer.line(previousX, previousY, currentX, currentY);

            previousX = currentX;
            previousY = currentY;
        }
        shapeRenderer.end();
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

    @Override
    public String getName() {
        return levyFlight.getName();
    }

    @Override
    public VisTable buildParametersTable() {
        final VisTextField alphaTextField = new VisTextField("" + levyFlight.getAlpha());
        final VisTextField cTextField = new VisTextField("" + levyFlight.getC());

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                levyFlight.setAlpha(Float.parseFloat(alphaTextField.getText()));
                levyFlight.setC(Float.parseFloat(cTextField.getText()));

                init();
                randomGenerator.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("alpha: "));
        parametersTable.add(alphaTextField);
        parametersTable.add(new VisLabel("c: "));
        parametersTable.add(cTextField).row();
        table.add(new VisLabel(levyFlight.getName() + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
