package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;

public class Bar extends AbstractFunction implements Function {

    @Override
    public void compute() {
        results = new ArrayList<>();
        results.add("Bar");
    }
}
