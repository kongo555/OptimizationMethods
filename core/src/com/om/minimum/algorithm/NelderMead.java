package com.om.minimum.algorithm;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.om.minimum.ConturPlot;
import com.om.minimum.Mapper;

public class NelderMead extends Algorithm {
    double alpha = 1.0;
    double beta = 0.5;
    double gamma = 2.0;
    double epsilon = 0.0000001;

    int n;

    int vs;         /* vertex with smallest value */
    int vh;         /* vertex with next smallest value */
    int vg;         /* vertex with largest value */

    int i, j, m, row;
    int k;   	      /* track the number of function evaluations */

    double simplexVertices[][];     /* holds vertices of simplex */
    double verticesZ[];      /* value of function at each vertex */
    double fr;      /* value of function at reflection point */
    double fe;      /* value of function at expansion point */
    double fc;      /* value of function at contraction point */
    double vr[];     /* reflection - coordinates */
    double ve[];     /* expansion - coordinates */
    double vc[];     /* contraction - coordinates */
    double vm[];     /* centroid - coordinates */
    double min;

    public NelderMead(ConturPlot conturPlot) {
        super("Nelder-Mead method", conturPlot);
    }

    public void init(Mapper mapper, int numberOfIterations) {
        this.mapper = mapper;
        this.numberOfIterations = numberOfIterations;
        double start[] = {mapper.randomX(), mapper.randomY()};
        n = 2;
        int scale = 1;

        simplexVertices = new double[n + 1][n];
        verticesZ = new double[n + 1];
        vr = new double[n];
        ve = new double[n];
        vc = new double[n];
        vm = new double[n];

        x = new double[simplexVertices.length];
        y = new double[simplexVertices.length];

        /* create the initial simplex */
        /* assume one of the vertices is 0,0 */
        double pn = scale * (Math.sqrt(n + 1) - 1 + n) / (n * Math.sqrt(2));
        double qn = scale * (Math.sqrt(n + 1) - 1) / (n * Math.sqrt(2));

        for (i = 0; i < n; i++) {
            simplexVertices[0][i] = start[i];
        }

        for (i = 1; i <= n; i++) {
            for (j = 0; j < n; j++) {
                if (i - 1 == j) {
                    simplexVertices[i][j] = pn + start[j];
                } else {
                    simplexVertices[i][j] = qn + start[j];
                }
            }
        }

        /* find the initial function values */
        for (j = 0; j <= n; j++) {
            verticesZ[j] = mapper.f(simplexVertices[j][0], simplexVertices[j][1]);
        }

        k = n + 1;

        /* print out the initial values */
       /* System.out.println("Initial Values");
        for (j=0;j<=n;j++) {
            for (i=0;i<n;i++) {
                System.out.println(simplexVertices[j][i] + " " + verticesZ[j]);
            }
        }*/
    }

    public void update(int iterationCounter) {
        // find the index of the largest value
        vg = 0;
        for (j = 0; j <= n; j++) {
            if (verticesZ[j] > verticesZ[vg]) {
                vg = j;
            }
        }

        // find the index of the smallest value
        vs = 0;
        for (j = 0; j <= n; j++) {
            if (verticesZ[j] < verticesZ[vs]) {
                vs = j;
            }
        }

        // find the index of the second largest value
        vh = vs;
        for (j = 0; j <= n; j++) {
            if (verticesZ[j] > verticesZ[vh] && verticesZ[j] < verticesZ[vg]) {
                vh = j;
            }
        }

        // calculate the centroid
        for (j = 0; j <= n - 1; j++) {
            double cent = 0.0;
            for (m = 0; m <= n; m++) {
                if (m != vg) {
                    cent += simplexVertices[m][j];
                }
            }
            vm[j] = cent / n;
        }

        // reflect vg to new vertex vr
        for (j = 0; j <= n - 1; j++) {
            vr[j] = vm[j] + alpha * (vm[j] - simplexVertices[vg][j]);
        }
        fr = mapper.f(vr[0], vr[1]);
        k++;

        if (fr < verticesZ[vh] && fr >= verticesZ[vs]) {
            for (j = 0; j <= n - 1; j++) {
                simplexVertices[vg][j] = vr[j];
            }
            verticesZ[vg] = fr;
        }

        // investigate a step further in this direction
        if (fr < verticesZ[vs]) {
            for (j = 0; j <= n - 1; j++) {
                ve[j] = vm[j] + gamma * (vr[j] - vm[j]);
            }
            fe = mapper.f(ve[0], ve[1]);
            k++;

            if (fe < fr) {
                for (j = 0; j <= n - 1; j++) {
                    simplexVertices[vg][j] = ve[j];
                }
                verticesZ[vg] = fe;
            } else {
                for (j = 0; j <= n - 1; j++) {
                    simplexVertices[vg][j] = vr[j];
                }
                verticesZ[vg] = fr;
            }
        }

        // check to see if a contraction is necessary
        if (fr >= verticesZ[vh]) {
            if (fr < verticesZ[vg] && fr >= verticesZ[vh]) {
                for (j = 0; j <= n - 1; j++) {
                    vc[j] = vm[j] + beta * (vr[j] - vm[j]);
                }
                fc = mapper.f(vc[0], vc[1]);
                k++;
            } else {
                for (j = 0; j <= n - 1; j++) {
                    vc[j] = vm[j] - beta * (vm[j] - simplexVertices[vg][j]);
                }
                fc = mapper.f(vc[0], vc[1]);
                k++;
            }


            if (fc < verticesZ[vg]) {
                for (j = 0; j <= n - 1; j++) {
                    simplexVertices[vg][j] = vc[j];
                }
                verticesZ[vg] = fc;
            } else {
                for (row = 0; row <= n; row++) {
                    if (row != vs) {
                        for (j = 0; j <= n - 1; j++) {
                            simplexVertices[row][j] = simplexVertices[vs][j] + (simplexVertices[row][j] - simplexVertices[vs][j]) / 2.0;
                        }
                    }
                }

                verticesZ[vg] = mapper.f(simplexVertices[vg][0], simplexVertices[vg][1]);
                k++;

                verticesZ[vh] = mapper.f(simplexVertices[vh][0], simplexVertices[vh][1]);
                k++;


            }
        }
        /*
		    *//* test for convergence *//*
            fsum = 0.0;
            for (j=0;j<=n;j++) {
                fsum += verticesZ[j];
            }
            favg = fsum/(n+1);
            s = 0.0;
            for (j=0;j<=n;j++) {
                s += Math.pow((verticesZ[j]-favg),2.0)/(n);
            }
            s = Math.sqrt(s);
            if (s < epsilon) break;

	    /* find the index of the smallest value */
        vs = 0;
        for (j = 0; j <= n; j++) {
            if (verticesZ[j] < verticesZ[vs]) {
                vs = j;
            }
        }

      /*  System.out.println("The minimum was found at\n");
        for (j=0;j<n;j++) {
            System.out.println(simplexVertices[vs][j]);
            //start[j] = simplexVertices[vs][j];
        }*/
        min = mapper.f(simplexVertices[vs][0], simplexVertices[vs][1]);
        k++;
        //System.out.println(k + " Function Evaluations");
        //System.out.println(itr + " Iterations through program");

        bindVariebles();
    }

    public void bindVariebles() {
        xBestGlobal = simplexVertices[vs][0];
        yBestGlobal = simplexVertices[vs][1];
        zBestGlobal = min;
        for (int i = 0; i < simplexVertices.length; i++) {
            x[i] = simplexVertices[i][0];
            y[i] = simplexVertices[i][1];
        }
    }

    @Override
    public VisTable buildParametersTable() {
        final VisTextField alphaTextField = new VisTextField("" + alpha);
        final VisTextField beta0TextField = new VisTextField("" + beta);
        final VisTextField gamaTextField = new VisTextField("" + gamma);

        final VisTextButton button = new VisTextButton("Confirm");
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                alpha = Double.parseDouble(alphaTextField.getText());
                beta = Double.parseDouble(beta0TextField.getText());
                gamma = Double.parseDouble(gamaTextField.getText());

                init(mapper, numberOfIterations);
                conturPlot.reset();
            }
        });

        VisTable table = new VisTable(true);
        VisTable parametersTable = new VisTable(true);
        parametersTable.add(new VisLabel("alpha: "));
        parametersTable.add(alphaTextField);
        parametersTable.add(new VisLabel("beta: "));
        parametersTable.add(beta0TextField).row();
        parametersTable.add(new VisLabel("gama: "));
        parametersTable.add(gamaTextField);
        table.add(new VisLabel(name + " Parameters:")).row();
        table.add(parametersTable).row();
        table.add(button);

        return table;
    }
}
