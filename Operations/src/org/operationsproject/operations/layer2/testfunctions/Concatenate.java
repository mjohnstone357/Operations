package org.operationsproject.operations.layer2.testfunctions;

import org.operationsproject.operations.layer2.Function;

import java.util.ArrayList;
import java.util.List;

public class Concatenate implements Function{

    private List<String> inputs;

    @Override
    public List<String> apply() {

        StringBuilder stringBuilder = new StringBuilder();
        for (String input : inputs) {
            stringBuilder.append(input);
        }

        List<String> outputs = new ArrayList<>();
        outputs.add(stringBuilder.toString());
        return outputs;
    }

    @Override
    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }
}
