package com.om.minimum;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.om.RendererValues;
import com.om.minimum.utils.Point;

import java.util.ArrayList;

/**
 * Created by kongo on 06.05.16.
 */
public class ConturPlotRenderer implements RendererValues {
    private Mapper mapper;
    private int width;
    private int height;
    private int textpaddingX = 20;
    private int textpaddingY = 50;
    private int xStep;
    private int yStep;

    private BitmapFont font;

    public ConturPlotRenderer(Mapper mapper, int width, int height) {
        this.mapper = mapper;
        this.width = width;
        this.height = height;
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("default.fnt"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 12;
//        font = generator.generateFont(parameter);
        font = new BitmapFont();
//        font.setColor(Color.BLACK);
        xStep = (int) ((mapper.getxMax() - mapper.getxMin()) / 10);
        yStep = (int) ((mapper.getyMax() - mapper.getyMin()) / 10);
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        float x0 = width - functionWidth - paddingX;
        float y0 = height - functionHeight - paddingY;
        mapper.render(spriteBatch, x0, y0, functionWidth, functionHeight);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        x0 = width - functionWidth - paddingX;
        y0 = height - functionHeight - paddingY;
        shapeRenderer.rect(x0, y0, functionWidth, functionHeight);
        shapeRenderer.end();

        spriteBatch.begin();
        //x - down text (5 for font size)
        int start = (int) mapper.getxMin();
        for (int i = 0; i < 11; i++) {
            x0 = width - functionWidth - paddingX - 5 + i * functionWidth / 10;
            y0 = height - functionHeight - textpaddingY;
            font.draw(spriteBatch, "" + start, x0, y0);
            start += xStep;
        }
        //y - left text (30 for font size)
        start = (int) mapper.getyMin();
        for (int i = 0; i < 11; i++) {
            x0 = width - functionWidth - paddingX - textpaddingX;
            y0 = height - functionHeight - 30 + i * functionHeight / 10;
            font.draw(spriteBatch, "" + start, x0, y0);
            start += yStep;
        }
        spriteBatch.end();
    }

    public void renderPoints(ShapeRenderer shapeRenderer, ArrayList<Point> points, Point bestPoint) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.PINK);
        float x0 = width - functionWidth - paddingX;
        float y0 = height - functionHeight - paddingY;
        float x, y;
        for (Point point : points) {
            x = (functionWidth * point.getX()) / mapper.getWidth();
            y = (functionHeight * point.getY()) / mapper.getHeight();
            shapeRenderer.rect(x0 + x, y0 + y, 5, 5);
        }
        shapeRenderer.setColor(Color.WHITE);
        x = (functionWidth * bestPoint.getX()) / mapper.getWidth();
        y = (functionHeight * bestPoint.getY()) / mapper.getHeight();
        shapeRenderer.circle(x0 + x, y0 + y, 6);
        shapeRenderer.end();
    }

    public void renderLines(ShapeRenderer shapeRenderer, ArrayList<Point> points, Point bestPoint) {
        if (!points.isEmpty()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.PINK);
            float previousX = (functionWidth * points.get(0).getX()) / mapper.getWidth();
            float previousY = (functionHeight * points.get(0).getY()) / mapper.getHeight();
            float x0 = width - functionWidth - paddingX;
            float y0 = height - functionHeight - paddingY;
            float x, y;
            for (int i = 1; i < points.size(); i++) {
                Point point = points.get(i);
                x = (functionWidth * point.getX()) / mapper.getWidth();
                y = (functionHeight * point.getY()) / mapper.getHeight();

                shapeRenderer.line(x0 + previousX, y0 + previousY, x0 + x, y0 + y);
                previousX = x;
                previousY = y;
            }
            x = (functionWidth * points.get(0).getX()) / mapper.getWidth();
            y = (functionHeight * points.get(0).getY()) / mapper.getHeight();
            shapeRenderer.line(x0 + x, y0 + y, x0 + previousX, y0 + previousY);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            x = (functionWidth * bestPoint.getX()) / mapper.getWidth();
            y = (functionHeight * bestPoint.getY()) / mapper.getHeight();
            shapeRenderer.circle(x0 + x, y0 + y, 4);
            shapeRenderer.end();
        }
    }

}
