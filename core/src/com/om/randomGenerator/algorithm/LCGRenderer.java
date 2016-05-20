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

/**
 * Created by kongo on 08.05.16.
 */
public class LCGRenderer extends RandomGeneratorRenderer {
    private LCG lcg;
    private boolean rectangles[][];
    private int totalCells;
    private int filledCells;

    private float gridSize;
    private int cellSize;

    public LCGRenderer(RandomGenerator randomGenerator, int width, int height) {
        super(randomGenerator, width, height);

        lcg = new LCG(1);
        totalCells = functionWidth * functionHeight;
        cellSize = 10;
    }

    public void init(){
        filledCells = 0;
        gridSize = functionWidth / cellSize;
        rectangles = new boolean[(int)gridSize][(int)gridSize];
    }

    public void update(){
        if(filledCells != totalCells) {
            Point point = lcg.generatePoint((int) gridSize);
            if (!rectangles[(int) point.getX()][(int) point.getY()]) {
                rectangles[(int) point.getX()][(int) point.getY()] = true;
                filledCells++;
            }
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        drawRectangles(shapeRenderer);
        drawGrid(shapeRenderer);
    }

    @Override
    public String getName() {
        return lcg.getName();
    }

    public void drawRectangles(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(rectangles[i][j])
                    shapeRenderer.setColor(Color.RED);
                else
                    shapeRenderer.setColor(Color.WHITE);

                shapeRenderer.rect(x0 + i * cellSize, y0 + j * cellSize, cellSize, cellSize);
            }
        }
        shapeRenderer.end();
    }

    private void drawGrid(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 0; i <= gridSize; i++)
            shapeRenderer.line(x0 + i * cellSize, y0, x0 + i * cellSize, y0 + functionHeight);

        for (int i = 0; i <= gridSize; i++)
            shapeRenderer.line(x0, y0 + i * cellSize, x0 + functionWidth, y0 + i * cellSize);
        shapeRenderer.end();
    }

    public VisTable buildParametersTable() {
        final VisTextField aTextField = new VisTextField("" + lcg.getA());
        final VisTextField cTextField = new VisTextField("" + lcg.getC());
        final VisTextField mTextField = new VisTextField("" + lcg.getM());
        final VisTextField seedTextField = new VisTextField("" + lcg.getSeed());
        //final VisTextField cellSizeTextField = new VisTextField("" + cellSize);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                lcg.setA(Integer.parseInt(aTextField.getText()));
                lcg.setC(Integer.parseInt(cTextField.getText()));
                lcg.setM(Long.parseLong(mTextField.getText()));
                lcg.setSeed(Long.parseLong(seedTextField.getText()));
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
        table.add(new VisLabel(lcg.getName() + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
