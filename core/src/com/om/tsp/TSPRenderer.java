package com.om.tsp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.om.RendererValues;
import com.om.minimum.utils.Point;

import java.util.ArrayList;

/**
 * Created by kongo on 09.05.16.
 */
public class TSPRenderer implements RendererValues {
    private ArrayList<Point> nodes;
    private int width;
    private int height;
    private int graphWidth;
    private int graphHeight;
    private float nodeSize;
    private float x0;
    private float y0;

    public TSPRenderer(ArrayList<Point> nodes, int width, int height) {
        this.nodes = nodes;
        this.width = width;
        this.height = height;

        x0 = width - functionWidth - paddingX;
        y0 = height - functionHeight - paddingY;

        graphWidth = 0;
        graphHeight = 0;
        nodeSize = 10;
        for (Point node : nodes) {
            if(node.getX() > graphWidth)
                graphWidth = (int)node.getX();
            if(node.getY() > graphHeight)
                graphHeight = (int)node.getY();
        }
        graphWidth += nodeSize * 2;
        graphHeight += nodeSize * 2;
    }

    public void render(ShapeRenderer shapeRenderer, int[] best) {
        renderBackground(shapeRenderer);
        renderPath(shapeRenderer, best);
        renderNodes(shapeRenderer);
    }

    public void renderNodes(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        float x, y;
        for (Point point: nodes) {
            x = x0 + (point.getX() * functionWidth) / graphWidth;
            y = y0 + (point.getY() * functionHeight) / graphHeight;
            shapeRenderer.rect(x , y, nodeSize, nodeSize);
        }
        shapeRenderer.end();
    }

    private void renderPath(ShapeRenderer shapeRenderer, int[] best){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        float x, y;
        float xPrevious = x0 + ((nodes.get(best[0]).getX() + nodeSize/2) * functionWidth) / graphWidth;
        float yPrevious = y0 + ((nodes.get(best[0]).getY() + nodeSize/2) * functionHeight) / graphHeight;
        for (int i = 1; i < best.length; i++) {
            x = x0 + ((nodes.get(best[i]).getX() + nodeSize/2) * functionWidth) / graphWidth;
            y = y0 + ((nodes.get(best[i]).getY() + nodeSize/2) * functionHeight) / graphHeight;
            shapeRenderer.line(xPrevious, yPrevious, x, y);
            xPrevious = x;
            yPrevious = y;
        }
        x = x0 + ((nodes.get(best[0]).getX() + nodeSize/2) * functionWidth) / graphWidth;
        y = y0 + ((nodes.get(best[0]).getY() + nodeSize/2) * functionHeight) / graphHeight;
        shapeRenderer.line(xPrevious, yPrevious, x, y);
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
}
