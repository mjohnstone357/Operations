package org.operationsproject.operations.layer2.testfunctions;

import java.util.ArrayList;

public class Foo extends AbstractFunction {

    @Override
    public void compute() {
        results = new ArrayList<>();
        results.add("Foo");
    }
}
