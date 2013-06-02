package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;

public class StringLiteral extends AbstractFunction implements Function {

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
