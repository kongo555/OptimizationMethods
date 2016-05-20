package com.om.randomGenerator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kotcrab.vis.ui.widget.VisTable;
import com.om.RendererValues;

/**
 * Created by kongo on 08.05.16.
 */
public abstract class RandomGeneratorRenderer implements RendererValues {
    protected RandomGenerator randomGenerator;
    protected int width;
    protected int height;
    protected float x0;
    protected float y0;

    public RandomGeneratorRenderer(RandomGenerator randomGenerator, int width, int height) {
        this.randomGenerator = randomGenerator;
        this.width = width;
        this.height = height;
        x0 = width - functionWidth - paddingX;
        y0 = height - functionHeight - paddingY;
    }

    public abstract void init();

    public abstract void update();

    public abstract void render(ShapeRenderer shapeRenderer);

    public abstract String getName();

    public abstract VisTable buildParametersTable();
}
