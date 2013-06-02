package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;

public class Foo extends AbstractFunction implements Function {

    @Override
    public void compute() {
        results = new ArrayList<>();
        results.add("Foo");
    }
}
