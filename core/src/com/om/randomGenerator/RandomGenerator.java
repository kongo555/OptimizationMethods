package com.om.randomGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.om.Main;
import com.om.Problem;
import com.om.ProblemGUI;
import com.om.randomGenerator.algorithm.GaussianRenderer;
import com.om.randomGenerator.algorithm.LCGRenderer;
import com.om.randomGenerator.algorithm.LevyFlightRenderer;

import java.util.ArrayList;

/**
 * Created by kongo on 08.05.16.
 */
public class RandomGenerator extends Problem {
    private ArrayList<RandomGeneratorRenderer> randomGeneratorRenderers;
    private RandomGeneratorRenderer randomGeneratorRenderer;
    private RandomGeneratorGUI randomGeneratorGUI;
    private int speed;
    private int iteration;

    public RandomGenerator(Main main) {
        super("Random Generator", main);
    }

    @Override
    public void show() {
        main.resetCamera();
        initAlgorithms();
        randomGeneratorGUI = new RandomGeneratorGUI(this);
        setRandomGeneratorRenderer(randomGeneratorRenderers.get(0));
        reset();
        speed = 1;
    }

    public void reset() {
        iteration = 0;
    }

    @Override
    public void hide() {
        randomGeneratorGUI = null;
        randomGeneratorRenderer = null;
        randomGeneratorRenderers = null;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < speed; i++) {
            randomGeneratorRenderer.update();
            iteration++;
        }

        randomGeneratorGUI.update(iteration);
    }

    @Override
    public void render() {
        randomGeneratorRenderer.render(main.getShapeRenderer());
    }

    private void initAlgorithms() {
        randomGeneratorRenderers = new ArrayList<RandomGeneratorRenderer>();
        randomGeneratorRenderers.add(new LCGRenderer(this, main.getWidth(), main.getHeight()));
        randomGeneratorRenderers.add(new GaussianRenderer(this, main.getWidth(), main.getHeight()));
        randomGeneratorRenderers.add(new LevyFlightRenderer(this, main.getWidth(), main.getHeight()));
    }

    public void setRandomGeneratorRenderer(RandomGeneratorRenderer randomGeneratorRenderer) {
        this.randomGeneratorRenderer = randomGeneratorRenderer;
        this.randomGeneratorRenderer.init();
        randomGeneratorGUI.setParametersTable(randomGeneratorRenderer.buildParametersTable());
        reset();
    }

    @Override
    public ProblemGUI getGUI() {
        return randomGeneratorGUI;
    }

    public RandomGeneratorRenderer getRandomGeneratorRenderer() {
        return randomGeneratorRenderer;
    }

    public void setSpeed(int value) {
        speed = value;
    }

    public ArrayList<RandomGeneratorRenderer> getRandomGeneratorRenderers() {
        return randomGeneratorRenderers;
    }
}
