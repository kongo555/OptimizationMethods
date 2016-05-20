package com.om;

/**
 * Created by kongo on 08.05.16.
 */
public abstract class Problem {
    private String name;
    protected Main main;

    public Problem(String name, Main main) {
        this.name = name;
        this.main = main;
    }

    public abstract void show();

    public abstract void hide();

    public abstract void update(float delta);

    public abstract void render();

    public String getName() {
        return name;
    }

    public abstract ProblemGUI getGUI();
}
