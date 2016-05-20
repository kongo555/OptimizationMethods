package com.om.tsp;

import com.badlogic.gdx.Gdx;
import com.om.Main;
import com.om.Problem;
import com.om.ProblemGUI;
import com.om.tsp.algorithm.Algorithm;
import com.om.tsp.algorithm.antColony.AntAlgorithm;
import com.om.tsp.algorithm.iwd.IWD_Algorithm;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kongo on 09.05.16.
 */
public class TSP extends Problem {
    private ArrayList<Algorithm> algorithms;
    private Algorithm algorithm;
    private ArrayList<Graph> graphs;
    private Graph graph;
    private TSPRenderer tspRenderer;
    private TSPGUI tspGUI;
    private float time, speed;
    private int iteration;

    public TSP(Main main) {
        super("Travelling Salesman", main);
    }

    @Override
    public void show() {
        main.resetCamera();
        initGraphs();
        graph = graphs.get(0);
        tspRenderer = new TSPRenderer(graph.getNodes(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        initAlgorithms();
        tspGUI = new TSPGUI(this);
        setAlgorithm(algorithms.get(0));


        time = speed = 0.1f;
        reset();
    }

    public void reset() {
        iteration = 0;
    }

    @Override
    public void hide() {
        tspRenderer = null;
        tspGUI = null;
        algorithm = null;
        algorithms = null;
        graph = null;
        graphs = null;
    }

    @Override
    public void update(float delta) {
        time += delta;
        if (time >= speed) {
            time = 0;

            algorithm.update();
            iteration++;
        }

        tspGUI.update(algorithm.getBestTourLength(), iteration);
    }

    @Override
    public void render() {
        tspRenderer.render(main.getShapeRenderer(), algorithm.getBestTour());
    }

    private void initAlgorithms() {
        algorithms = new ArrayList<Algorithm>(2);
        algorithms.add(new AntAlgorithm(this));
        algorithms.add(new IWD_Algorithm(this));
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        algorithm.init(graph.getNodes());
        algorithm.update();
        tspGUI.setParametersTable(algorithm.buildParametersTable());
        reset();
    }

    private void initGraphs() {
        graphs = new ArrayList<Graph>(3);
        try {
            graphs.add(Utils.readFile("berlin.tsp"));
            graphs.add(Utils.readFile("ch130.tsp"));
            graphs.add(Utils.readFile("kroC100.tsp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGraph(Graph graph){
        this.graph = graph;
        algorithm.init(graph.getNodes());
        algorithm.update();
        tspRenderer = new TSPRenderer(graph.getNodes(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        reset();
    }

    @Override
    public ProblemGUI getGUI() {
        return tspGUI;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public ArrayList<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public ArrayList<Graph> getGraphs() {
        return graphs;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setSpeed(float value) {
        speed = value;
    }
}
