package com.om.minimum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by kongo on 13.03.16.
 */
public abstract class Mapper {
    protected int width;
    protected int height;
    protected float xMin;
    protected float yMin;
    protected float xMax;
    protected float yMax;
    protected double highestZ;
    protected double lowestX, lowestY, lowestZ;
    protected float step = 0.01f;

    protected TextureRegion texture;

    public Mapper(float xMin, float xMax, float yMin, float yMax, float step) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.step = step;
        width = (int) ((xMax - xMin) / step);
        height = (int) ((yMax - yMin) / step);
    }

    public void init(ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        findHighestAndLowest();
        Color[][] plot = createColorMap();
        camera.setToOrtho(false, width, height);
        shapeRenderer.setProjectionMatrix(camera.combined);
        texture = createTextureRegion(shapeRenderer, plot);
    }

    public abstract double f(double x, double y);

    private void findHighestAndLowest() {
        double current;
        highestZ = Float.MIN_VALUE;
        lowestZ = Float.MAX_VALUE;

        for (float x = xMin; x < xMax; x += step) {
            for (float y = yMin; y < yMax; y += step) {
                x = round(x, (int) (1 / step));
                y = round(y, (int) (1 / step));
                current = f(x, y);
                if (current > highestZ)
                    highestZ = current;
                if (current < lowestZ) {
                    lowestZ = current;
                    lowestX = x;
                    lowestY = y;
                }
            }
        }
    }

    private Color[][] createColorMap() {
        Color[][] plot = new Color[width][height];
        int i = 0;
        int j = 0;
        for (float x = xMin; x < xMax; x += step) {
            for (float y = yMin; y < yMax; y += step) {
                if (i < width && j < height) {
                    //x = round(x, (int)(1/step));
                    //y = round(y, (int)(1/step));
                    plot[i][j] = color((float) f(x, y), (float) highestZ);
                }
                j++;
            }
            j = 0;
            i++;
        }

        return plot;
    }

    public Color color(float x) {
        return new Color(2.0f * x, 2.0f * (1 - x), 0, 1);
    }

    // x from 0 2;
    // 2 - red
    // 1.5 = yellow
    // 1 - green
    // 0.5 - cyan
    // 0 - blue
    public Color color(float x, float range) {
        x /= range;
        x *= 2;
        if (x >= 1) {
            return new Color(2.0f * (x - 1), 2.0f * (2 - x), 0, 1);
        } else {
            return new Color(0, 2.0f * x, 2.0f * (1 - x), 1);
        }

//        return new Color(2.0f * x, 2.0f * (1 - x),0, 1);
    }

    private float round(float number, int precision) {
        number *= precision;
        number = Math.round(number);
        number /= precision;
        return number;
    }

    private void render(ShapeRenderer shapeRenderer, Color[][] plot) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                shapeRenderer.setColor(plot[i][j]);
                shapeRenderer.rect(i, j, 1, 1);
            }
        }
        shapeRenderer.end();
    }

    public void render(SpriteBatch spriteBatch, float x, float y, float width, float height) {
        spriteBatch.begin();
        spriteBatch.draw(texture, x, y, width, height);
        spriteBatch.end();
    }

    private TextureRegion createTextureRegion(ShapeRenderer shapeRenderer, Color[][] plot) {
        FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
        TextureRegion fboRegion;
        frameBuffer.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        render(shapeRenderer, plot);
        frameBuffer.end();

        fboRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        fboRegion.flip(false, true);

        return fboRegion;
    }

    public double randomX() {
        return MathUtils.random() * (xMax - xMin) + xMin;
    }

    public double randomY() {
        return MathUtils.random() * (yMax - yMin) + yMin;
    }

    public float xToI(double x) {
        return (float) ((x - xMin) / step);
    }

    public float yToJ(double y) {
        return (float) ((y - yMin) / step);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getxMin() {
        return xMin;
    }

    public float getyMin() {
        return yMin;
    }

    public float getxMax() {
        return xMax;
    }

    public float getyMax() {
        return yMax;
    }

    public double getHighestZ() {
        return highestZ;
    }

    public double getLowestX() {
        return lowestX;
    }

    public double getLowestY() {
        return lowestY;
    }

    public double getLowestZ() {
        return lowestZ;
    }

    public float getStep() {
        return step;
    }
}
