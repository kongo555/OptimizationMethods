package com.om.minimum;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Created by kongo on 07.05.16.
 */
public class FunctionMapper extends Mapper {
    Expression expression;

    public FunctionMapper(String text, float xMin, float xMax, float yMin, float yMax) {
        super(xMin, xMax, yMin, yMax, 0.01f);
        expression = new ExpressionBuilder(text)
                .variables("x", "y")
                .build();
    }

    public double f(double x, double y) {
        expression.setVariable("x", x)
                .setVariable("y", y);
        return expression.evaluate();
    }
}
