package com.om.minimum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.om.Main;
import com.om.Problem;
import com.om.ProblemGUI;
import com.om.minimum.algorithm.*;
import com.om.minimum.algorithm.css.CSS;
import com.om.minimum.algorithm.genetic.GeneticAlgorithm;
import com.om.minimum.utils.Function;
import com.om.minimum.utils.Point;

import java.util.ArrayList;

/**
 * Created by kongo on 06.05.16.
 */
public class ConturPlot extends Problem {
    private ConturPlotRenderer conturPlotRenderer;

    private ConturPlotGUI conturPlotGUI;

    private Mapper mapper;
    private Algorithm algorithm;
    private ArrayList<Point> points;
    private Point bestPoint;
    private float time, speed;
    private int iteration, numberOfIterations;

    private ArrayList<Algorithm> algorithms;
    private ArrayList<Function> functions;

    public ConturPlot(Main main) {
        super("Finding Minimum", main);
    }

    public void show(){
        points = new ArrayList<Point>(1000);
        bestPoint = new Point(0, 0);
        numberOfIterations = 1000;
        time = speed = 0.1f;

        initAlgorithms();
        algorithm = algorithms.get(0);
        initFunctions();
        setFunction(functions.get(0));

        conturPlotGUI = new ConturPlotGUI(this);
    }

    public void hide(){
        mapper = null;
        algorithm = null;
        points = null;
        bestPoint = null;
        algorithms = null;
        functions = null;
        conturPlotGUI = null;
    }

    public void reset() {
        main.resetCamera();

        iteration = 0;
        points.clear();
    }

    public void update(float delta) {
        time += delta;
        if (time >= speed) {
            time = 0;
            if (iteration < numberOfIterations) {
                algorithm.update(iteration);
                iteration++;
            }

            points.clear();
            double[] x = algorithm.getX();
            double[] y = algorithm.getY();
            for (int j = 0; j < x.length; j++) {
                if (x[j] > mapper.getxMin() && x[j] < mapper.getxMax() &&
                        y[j] > mapper.getyMin() && y[j] < mapper.getyMax())
                    points.add(new Point(mapper.xToI(x[j]), mapper.yToJ(y[j])));
            }
            conturPlotGUI.update(algorithm.getxBestGlobal(), algorithm.getyBestGlobal(), algorithm.getzBestGlobal(), iteration);
            bestPoint.set(mapper.xToI(algorithm.getxBestGlobal()), mapper.yToJ(algorithm.getyBestGlobal()));
        }
    }

    @Override
    public void render() {
        conturPlotRenderer.render(main.getSpriteBatch(), main.getShapeRenderer());
        if (algorithm instanceof NelderMead)
            conturPlotRenderer.renderLines(main.getShapeRenderer(), points, bestPoint);
        else
            conturPlotRenderer.renderPoints(main.getShapeRenderer(), points, bestPoint);
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        algorithm.init(mapper, numberOfIterations);
        conturPlotGUI.setParametersTable(algorithm.buildParametersTable());
        reset();
    }

    public void setFunction(Function function) {
        mapper = new FunctionMapper(function.getText(), function.getxMin(), function.getxMax(), function.getyMin(), function.getyMax());
        mapper.init(main.getShapeRenderer(), main.getCamera());
        conturPlotRenderer = new ConturPlotRenderer(mapper, main.getWidth(), main.getHeight());
        algorithm.init(mapper, numberOfIterations);
        reset();
    }

    private void initAlgorithms() {
        algorithms = new ArrayList<Algorithm>(8);
        algorithms.add(new SimulatedAnnealing(this));
        algorithms.add(new FireflyAlgorithm(this));
        algorithms.add(new BatAlgorithm(this));
        algorithms.add(new PSO(this));
        algorithms.add(new CSS(this));
        algorithms.add(new GeneticAlgorithm(this));
        algorithms.add(new EvolutionStrategy(this));
        algorithms.add(new NelderMead(this));
    }

    private void initFunctions() {
        functions = new ArrayList<Function>(2);
        functions.add(new Function("Rosenbrock", "(1-x)*(1-x) + 100 *(y - x*x)*(y - x*x)", -5, 5, -5, 5));
        functions.add(new Function("Booths", "(x + 2 * y - 7)*(x + 2 * y - 7)+ (2*x + y - 5)*(2*x + y - 5)", -10, 10, -10, 10));
    }

    public void setSpeed(float value) {
        speed = value;
    }

    @Override
    public ProblemGUI getGUI() {
        return conturPlotGUI;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public ArrayList<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }
}
