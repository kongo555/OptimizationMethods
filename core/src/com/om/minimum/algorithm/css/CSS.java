package com.om.minimum.algorithm.css;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.ConturPlot;
import com.om.minimum.algorithm.Algorithm;
import com.om.minimum.Mapper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by kongo on 22.03.16.
 */
public class CSS extends Algorithm {

    private int population = 20;
    private ArrayList<ChargedParticle> particles;

    private ChargedParticle best;
    private ChargedParticle worst;

    private ArrayList<ChargedParticle> chargedMemory;

    private double a = 1;
    private double kv, ka;
    int numberOfIterations;
    private Mapper mapper;

    public CSS(ConturPlot conturPlot) {
        super("Charged System Search", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;
        particles = new ArrayList<ChargedParticle>(population);
        chargedMemory = new ArrayList<ChargedParticle>(population / 4);
        x = new double[population];
        y = new double[population];

        for (int i = 0; i < population; i++) {
            ChargedParticle particle = new ChargedParticle(mapper.randomX(), mapper.randomY());
            particle.updateZ(mapper);
            particles.add(particle);
        }

        Collections.sort(particles);

        for (int i = 0; i < population / 4; i++) {
            chargedMemory.add(particles.get(i));
        }
        best = particles.get(0);
        worst = particles.get(particles.size() - 1);

        ka = 0;
        kv = 0;
    }

    public void update(int iterationCounter) {
        ChargedParticle iParticle, jParticle;
        double fx, fy, tmp, alpha, beta, r, p;

        double l, m;
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).updateQ(worst.getZ(), best.getZ());
        }

        for (int j = 0; j < particles.size(); j++) {
            jParticle = particles.get(j);

            // Krok 1: Określenie siły oddziaływania.
            fx = fy = 0;
            for (int i = 0; i < particles.size(); i++) {
                if (i == j)
                    continue;
                iParticle = particles.get(i);

                l = (jParticle.getX() - iParticle.getX()) * (jParticle.getX() - iParticle.getX());
                l += (jParticle.getY() - iParticle.getY()) * (jParticle.getY() - iParticle.getY());
                l = (float) Math.sqrt(l);

                m = (0.5f * (iParticle.getX() + jParticle.getX()) - best.getX()) * (0.5f * (iParticle.getX() + jParticle.getX()) - best.getX());
                m += (0.5f * (iParticle.getY() + jParticle.getY()) - best.getY()) * (0.5f * (iParticle.getY() + jParticle.getY()) - best.getY());
                m = (float) Math.sqrt(m);

                r = l / (m + Float.MIN_NORMAL);

                //TODO check other
                if ((iParticle.getZ() - best.getZ()) / (jParticle.getZ() - iParticle.getZ()) > MathUtils.random() || jParticle.getZ() > iParticle.getZ())
                    p = 1;
                else
                    p = 0;

                if (r < a) {
                    alpha = 1;
                    beta = 0;
                } else {
                    alpha = 0;
                    beta = 1;
                }

                tmp = (alpha * iParticle.getQ() / (a * a * a) * r + beta * iParticle.getQ() / (r * r)) * p;
                fx += tmp * (iParticle.getX() - jParticle.getX());
                fy += tmp * (iParticle.getY() - jParticle.getY());
            }

            // Krok 2: Tworzenie rozwiązania.
            ka = 0.5f * (1 + (double) iterationCounter / (double) numberOfIterations);
            kv = 0.5f * (1 - (double) iterationCounter / (double) numberOfIterations);

            double xOld = jParticle.getX();
            double yOld = jParticle.getY();

            jParticle.setX(jParticle.getX() + MathUtils.random() * kv * jParticle.getVx() +
                    MathUtils.random() * ka * fx);
            jParticle.setY(jParticle.getY() + MathUtils.random() * kv * jParticle.getVy() +
                    MathUtils.random() * ka * fy);

            jParticle.setVx((jParticle.getX() - xOld));
            jParticle.setVy((jParticle.getY() - yOld));

            // TODO Krok 3: Skorygowanie położeń CP

        }
        // Krok 4: Ranking CP
        for (ChargedParticle particle : particles) {
            particle.updateZ(mapper);
        }
        Collections.sort(particles);
        best = particles.get(0);
        worst = particles.get(particles.size() - 1);

        // Krok 5: Aktualizacja CM
        for (int i = 0; i < chargedMemory.size(); i++) {
            chargedMemory.set(i, particles.get(i));
        }

        bindVariebles();
    }

    public void bindVariebles() {
        xBestGlobal = best.getX();
        yBestGlobal = best.getY();
        zBestGlobal = best.getZ();

        for (int i = 0; i < particles.size(); i++) {
            x[i] = particles.get(i).getX();
            y[i] = particles.get(i).getY();
        }
    }


    @Override
    public VisTable buildParametersTable() {
        final VisTextField aTextField = new VisTextField("" + a);
        final VisTextField populationTextField = new VisTextField("" + population);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                a = Double.parseDouble(aTextField.getText());
                population = Integer.parseInt(populationTextField.getText());

                init(mapper, numberOfIterations);

                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("a: "));
        parametersTable.add(aTextField);
        parametersTable.add(new VisLabel("Population: "));
        parametersTable.add(populationTextField).row();

        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
