package com.om;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.om.minimum.ConturPlot;

/**
 * Created by kongo on 08.05.16.
 */
public class GUI {
    private Stage stage;
    private Table rootTable;
    private VisWindow menuWindow;
    private Table problemTable;
    private Main main;

    public GUI(Main main) {
        this.main = main;
        stage = new Stage(new ScreenViewport());

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top().left().padLeft(150).padTop(40);
        menuWindow = new VisWindow("Menu");
        menuWindow.top();
        menuWindow.setMovable(false);

        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(buildProblemMenu());

        menuWindow.add(menuBar.getTable()).padBottom(30).row();
        rootTable.add(menuWindow).prefWidth(500).prefHeight(900);

        stage.addActor(rootTable);
        Gdx.input.setInputProcessor(stage);
    }

    private Menu buildProblemMenu() {
        final Menu functionMenu = new Menu("Choose Problem");
        for (final Problem problem : main.getProblems()) {
            functionMenu.addItem(new MenuItem(problem.getName(), new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                   main.setProblem(problem);
                }
            }));
        }

        return functionMenu;
    }

    public void setProblemTable(ProblemGUI problemGUI){
        if(problemGUI != null) {
            menuWindow.removeActor(problemTable);
            problemTable = problemGUI.getTable();
            menuWindow.add(problemTable);
        }
    }

    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
