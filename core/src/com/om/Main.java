package com.om;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kotcrab.vis.ui.VisUI;
import com.om.minimum.ConturPlot;
import com.om.randomGenerator.RandomGenerator;
import com.om.tsp.TSP;

import java.util.ArrayList;

//TODO algorytm genetyczny binarny
public class Main extends ApplicationAdapter {
    private int width;
    private int height;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private GUI gui;
    private Problem problem;

    private ArrayList<Problem> problems = new ArrayList<Problem>();


    @Override
    public void create() {
        VisUI.load();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        initProblemms();
        gui = new GUI(this);
        setProblem(problems.get(0));
    }



    @Override
    public void render() {
        problem.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        problem.render();
        gui.render();
    }

    private void initProblemms() {
        problems.add(new RandomGenerator(this));
        problems.add(new ConturPlot(this));
        problems.add(new TSP(this));
    }

    public void setProblem (Problem problem) {
        if (this.problem != null)
            this.problem.hide();
        this.problem = problem;
        if (this.problem != null) {
            this.problem.show();
            gui.setProblemTable(problem.getGUI());
        }
    }

    public void resetCamera(){
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void resize(int width, int height) {
        gui.resize(width, height);
    }

    @Override
    public void dispose() {
        gui.dispose();
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public ArrayList<Problem> getProblems() {
        return problems;
    }
}
