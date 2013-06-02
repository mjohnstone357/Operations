package org.operationsproject.operations.layer2.testfunctions;

import java.util.ArrayList;

public class Bar extends AbstractFunction {

    @Override
    public void compute() {
        results = new ArrayList<>();
        results.add("Bar");
    }
}
