package org.operationsproject.operations.layer2.testfunctions;

import java.util.ArrayList;

public class Concatenate extends AbstractFunction {

    @Override
    public void compute() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String input : inputs) {
            stringBuilder.append(input);
        }

        results = new ArrayList<>();
        results.add(stringBuilder.toString());

    }
}
