package org.operationsproject.operations.layer2.testfunctions;

import java.util.ArrayList;

public class StringLiteral extends AbstractFunction {

    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public void compute() {
        results = new ArrayList<>();
        results.add(value);
    }
}
